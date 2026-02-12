package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.toUiText
import com.nghianguyen.consume.ui.AddBrandDialogState
import com.nghianguyen.consume.ui.beer.AddBeerDialogState
import com.nghianguyen.consume.ui.beer.BeerAddDialogType
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerAction
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerDialogState
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerEvent
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.drinks.repository.BeerRepository
import com.nghianguyen.drinks.usecase.AddBeerBrandUseCase
import com.nghianguyen.drinks.usecase.AddBeerUseCase
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.GetBeersByBrandUseCase
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
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
) : ConsumeDrinkViewModel<ConsumeBeerDialogState, ConsumeBeerAction, ConsumeBeerEvent>(
    addConsumedDrinkUseCase
) {
    override fun buildInitialState() = ConsumeBeerDialogState(
        beerStyles = emptyList(),
        beerBrands = emptyList(),
        beers = emptyList(),
        selectedStyle = null,
        selectedBrand = null,
        selectedBeer = null,
        addDialogState = null,
        errorMsg = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            beerRepository.getBeerStyles().onResult(
                onSuccess = { beerStyles -> updateState { copy(beerStyles = beerStyles) } },
                onFailure = {}
            )
        }

        launch {
            beerRepository.getBeerBrands()
                .collect { beerBrandsResult ->
                    beerBrandsResult.onResult(
                        onSuccess = { beerBrands -> updateState { copy(beerBrands = beerBrands) } },
                        onFailure = {}
                    )
                }
        }

        launch {
            uiState.map { it.selectedBrand }
                .distinctUntilChanged()
                .flatMapLatest { brand ->
                    brand?.let {
                        getBeersByBrandUseCase(it)
                    } ?: flowOf(Ok(emptyList()))
                }.collect { beersByBrandResult ->
                    beersByBrandResult.onResult(
                        onSuccess = { beers ->
                            val selectedBeer = uiState.value.selectedBeer
                            val newSelectedBeer =
                                selectedBeer?.takeIf { selected ->
                                    beers.any { selected.beerId == it.beerId}
                                }
                            updateState { copy(beers = beers, selectedBeer = newSelectedBeer) }
                        },
                        onFailure = {}
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeBeerAction) {
        Log.d("ConsumeBeerViewModel", "handleAction: $action")
        when (action) {
            is ConsumeBeerAction.StyleSelected -> {
                selectStyle(action.selectedStyle)
            }
            is ConsumeBeerAction.BrandSelected -> {
                selectBrand(action.selectedBrand)
            }
            is ConsumeBeerAction.BeerSelected -> {
                selectBeer(action.selectedBeer)
            }
            ConsumeBeerAction.OpenAddBeerDialog -> {
                openAddBeerDialog()
            }
            is ConsumeBeerAction.OpenAddBrandDialog ->  {
                openAddBrandDialog()
            }
            is ConsumeBeerAction.DismissAddDialog -> {
                updateState { copy(addDialogState = null) }
            }
            is ConsumeBeerAction.AddBrand -> {
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

    private fun selectStyle(style: BeerStyle?) {
        updateState { copy(selectedStyle = style, selectedBeer = null) }
    }

    private fun selectBrand(brand: BeerBrand?) {
        updateState { copy(selectedBrand = brand, selectedBeer = null) }
    }

    private fun selectBeer(beeer: Drink.Beer?) {
        updateState {
            copy(
                selectedBeer = beeer,
                selectedStyle = beeer?.style
            )
        }
    }

    private fun openAddBeerDialog() {
        val uiState = uiState.value
        val addBeerDialogState = AddBeerDialogState(
            beerStyles = uiState.beerStyles,
            beerBrands = uiState.beerBrands
        )
        updateState {
            copy(addDialogState = BeerAddDialogType.AddBeer(addBeerDialogState))
        }
    }

    private fun openAddBrandDialog() {
        updateState {
            copy(addDialogState = BeerAddDialogType.AddBrand(AddBrandDialogState()))
        }
    }

    private fun addBeerBrand(name: String) {
        launch {
            addBeerBrandUseCase(name).onResult(
                onSuccess = { beerBrand ->
                    updateState {
                        copy(
                            selectedBrand = beerBrand,
                            selectedBeer = null,
                            beers = emptyList(),
                            addDialogState = null
                        )
                    }
                },
                onFailure = { error ->
                    val addDialogState =
                        when (val currentAddDialogState = uiState.value.addDialogState) {
                            is BeerAddDialogType.AddBrand -> {
                                currentAddDialogState.copy(
                                    addDialogState = currentAddDialogState.addDialogState.copy(
                                        errorMsg = error.toUiText()
                                    )
                                )
                            }
                            else -> currentAddDialogState
                        }
                    updateState { copy(addDialogState = addDialogState) }
                }
            )
        }
    }

    private fun addBeer(name: String, brand: BeerBrand, style: BeerStyle) {
        launch {
            addBeerUseCase(name, brand, style).onResult(
                onSuccess = { beerId ->
                    val newBeer = Drink.Beer(beerId.toInt(),
                        name = name,
                        brand = brand,
                        style = style
                    )
                    updateState {
                        copy(
                            selectedStyle = style,
                            selectedBrand = brand,
                            selectedBeer = newBeer,
                            addDialogState = null
                        )
                    }
                },
                onFailure = { error ->
                    val addDialogState =
                        when (val currentAddDialogState = uiState.value.addDialogState) {
                            is BeerAddDialogType.AddBeer -> {
                                currentAddDialogState.copy(
                                    addDialogState =
                                        currentAddDialogState.addDialogState.copy(
                                            errorMsg = error.toUiText()
                                        )
                                )
                            }
                            else -> currentAddDialogState
                        }

                    updateState { copy(addDialogState = addDialogState) }
                }
            )
        }
    }

    private fun submitConsumedBeer() {
        launch {
            uiState.value.selectedBeer?.let {
                addConsumedDrink(it).onResult(
                    onSuccess = {
                        sendEvent(ConsumeBeerEvent.SubmitConsumedBeerSuccess)
                    },
                    onFailure = { error ->
                        updateState { copy(errorMsg = error.toUiText()) }
                    }
                )
            }
        }
    }
}
