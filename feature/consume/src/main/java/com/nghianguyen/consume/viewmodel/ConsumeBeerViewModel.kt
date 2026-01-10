package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.ResourcesHelper
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerAction
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerEvent
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.drinks.repository.BeerRepository
import com.nghianguyen.drinks.usecase.AddBeerBrandUseCase
import com.nghianguyen.drinks.usecase.AddBeerUseCase
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.GetBeersByBrandUseCase
import com.nghianguyen.drinks.usecase.request.AddBeerRequest
import com.nghianguyen.drinks.usecase.request.AddBrandRequest
import com.nghianguyen.drinks.usecase.request.GetBeersByBrandRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ConsumeBeerViewModel @Inject constructor(
    private val beerRepository: BeerRepository,
    private val getBeersByBrandUseCase: GetBeersByBrandUseCase,
    private val addBeerBrandUseCase: AddBeerBrandUseCase,
    private val addBeerUseCase: AddBeerUseCase,
    private val resourceHelper: ResourcesHelper,
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
) : ConsumeDrinkViewModel<ConsumeBeerState, ConsumeBeerAction, ConsumeBeerEvent>(
    addConsumedDrinkUseCase
) {
    override fun buildInitialState() = ConsumeBeerState(
        beerStyles = emptyList(),
        beerBrands = emptyList(),
        beers = emptyList(),
        selectedStyle = null,
        selectedBrand = null,
        selectedBeer = null,
        errorMsg = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            handleResult(
                beerRepository.getBeerStyles(),
                { beerStyles -> updateState { copy(beerStyles = beerStyles) } },
                {}
            )
        }

        launch {
            beerRepository.getBeerBrands()
                .collect { beerBrandsResult ->
                    handleResult(
                        beerBrandsResult,
                        { beerBrands -> updateState { copy(beerBrands = beerBrands) } },
                        {}
                    )
                }
        }

        launch {
            uiState.map { it.selectedBrand }
                .distinctUntilChanged()
                .flatMapLatest { selectedBeerBrand ->
                    if (selectedBeerBrand != null) {
                        getBeersByBrandUseCase(GetBeersByBrandRequest(selectedBeerBrand))
                    } else {
                        flowOf(Ok(emptyList()))
                    }
                }.collect { beersByBrandResult ->
                    handleResult(
                        beersByBrandResult,
                        { beers ->
                            val currentSelectedBeer = uiState.value.selectedBeer
                            val newSelectedBeer =
                                if (beers.map { it.beerId }
                                        .contains(currentSelectedBeer?.beerId)) currentSelectedBeer
                                else null
                            updateState { copy(beers = beers, selectedBeer = newSelectedBeer) }
                        },
                        { }
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeBeerAction) {
        Log.d("ConsumeBeerViewModel", "handleAction: $action")
        launch {
            when (action) {
                is ConsumeBeerAction.BeerStyleSelected -> {
                    updateState {
                        copy(selectedStyle = action.selectedStyle, selectedBeer = null)
                    }
                }
                is ConsumeBeerAction.BeerBrandSelected -> {
                    updateState { copy(selectedBrand = action.selectedBrand) }
                }
                is ConsumeBeerAction.BeerSelected -> {
                    updateState {
                        copy(
                            selectedBeer = action.selectedBeer,
                            selectedStyle = action.selectedBeer?.style
                        )
                    }
                }
                is ConsumeBeerAction.AddBeerBrand -> {
                    addBeerBrand(action.brandName)
                }
                is ConsumeBeerAction.AddBeer -> {
                    addBeer(action.name, action.beerBrand, action.beerStyle)
                }
                is ConsumeBeerAction.SubmitConsumedBeer -> {
                    submitConsumedBeer()
                }
            }
        }
    }

    private suspend fun addBeerBrand(name: String) {
        handleResult(
            addBeerBrandUseCase(AddBrandRequest(name)),
            { beerBrand ->
                updateState {
                    copy(
                        selectedBrand = beerBrand,
                        selectedBeer = null,
                        beers = emptyList()
                    )
                }
                sendEvent(ConsumeBeerEvent.AddBeerBrandSuccess)
            },
            { error ->
                val errorMsg = resourceHelper.getString(error.stringRes)
                sendEvent(ConsumeBeerEvent.AddBeerBrandError(errorMsg))
            }
        )
    }

    private suspend fun addBeer(name: String, brand: BeerBrand, style: BeerStyle) {
        val addBeerRequest =
            AddBeerRequest(name, brand, style)
        handleResult(
            addBeerUseCase(addBeerRequest),
            { beerId ->
                val newBeer = Drink.Beer(
                    beerId = beerId.toInt(),
                    style = style,
                    brand = brand,
                    name = name
                )
                updateState {
                    copy(
                        selectedStyle = style,
                        selectedBrand = brand,
                        selectedBeer = newBeer
                    )
                }
                sendEvent(ConsumeBeerEvent.AddBeerSuccess)
            },
            { error ->
                val errorMsg = resourceHelper.getString(error.stringRes)
                sendEvent(ConsumeBeerEvent.AddBeerError(errorMsg))
            }
        )
    }

    private suspend fun submitConsumedBeer() {
        uiState.value.selectedBeer?.let {
            handleResult(
                addConsumedDrink(it),
                {
                    sendEvent(ConsumeBeerEvent.SubmitConsumedBeerSuccess)
                },
                { error ->
                    val errorMsg = resourceHelper.getString(error.stringRes)
                    updateState { copy(errorMsg = errorMsg) }
                    sendEvent(ConsumeBeerEvent.SubmitConsumedBeerError(errorMsg))
                }
            )
        }
    }
}
