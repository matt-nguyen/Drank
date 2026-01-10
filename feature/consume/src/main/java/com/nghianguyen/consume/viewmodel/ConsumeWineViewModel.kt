package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.ResourcesHelper
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineAction
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineEvent
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import com.nghianguyen.drinks.repository.WineRepository
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.AddWineBrandUseCase
import com.nghianguyen.drinks.usecase.AddWineUseCase
import com.nghianguyen.drinks.usecase.GetWinesByBrandUseCase
import com.nghianguyen.drinks.usecase.request.AddBrandRequest
import com.nghianguyen.drinks.usecase.request.AddWineRequest
import com.nghianguyen.drinks.usecase.request.GetWinesByBrandRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ConsumeWineViewModel @Inject constructor(
    private val wineRepository: WineRepository,
    private val getWinesByBrandUseCase: GetWinesByBrandUseCase,
    private val addWineBrandUseCase: AddWineBrandUseCase,
    private val addWineUseCase: AddWineUseCase,
    private val resourceHelper: ResourcesHelper,
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
) : ConsumeDrinkViewModel<ConsumeWineState, ConsumeWineAction, ConsumeWineEvent>(
    addConsumedDrinkUseCase
) {
    override fun buildInitialState() = ConsumeWineState(
        wineStyles = emptyList(),
        wineBrands = emptyList(),
        wines = emptyList(),
        selectedStyle = null,
        selectedBrand = null,
        selectedWine = null,
        errorMsg = null
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            handleResult(
                wineRepository.getWineStyles(),
                { wineStyles ->
                    updateState { copy(wineStyles = wineStyles) }
                },
                { }
            )
        }
        launch {
            wineRepository.getWineBrands()
                .collect { wineBrandsResult ->
                    handleResult(
                        wineBrandsResult,
                        { wineBrands ->
                            updateState { copy(wineBrands = wineBrands) }
                        },
                        { }
                    )
                }
        }

        launch {
            uiState.map { it.selectedBrand }
                .distinctUntilChanged()
                .flatMapLatest { selectedWineBrand ->
                    if (selectedWineBrand != null) {
                        getWinesByBrandUseCase(GetWinesByBrandRequest(selectedWineBrand))
                    } else {
                        flowOf(Ok(emptyList()))
                    }
                }.collect { winesByBrandResult ->
                    handleResult(
                        winesByBrandResult,
                        { wines ->
                            val selectedWine = uiState.value.selectedWine
                            val newSelectedWine =
                                if (wines.map { it.wineId }
                                        .contains(selectedWine?.wineId)) selectedWine
                                else null
                            updateState { copy(wines = wines, selectedWine = newSelectedWine) }
                        },
                        { }
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeWineAction) {
        Log.d("ConsumeWineViewModel", "handleAction: $action")
        launch {
            when (action) {
                is ConsumeWineAction.WineBrandSelected -> {
                    updateState { copy(selectedBrand = action.selectedBrand) }
                }

                is ConsumeWineAction.WineStyleSelected -> {
                    updateState { copy(selectedStyle = action.selectedStyle) }
                }

                is ConsumeWineAction.WineSelected -> {
                    updateState {
                        copy(
                            selectedWine = action.selectedWine,
                            selectedStyle = action.selectedWine?.style
                        )
                    }
                }

                is ConsumeWineAction.AddWineBrand -> {
                    addWineBrand(action.brandName)
                }

                is ConsumeWineAction.AddWine -> {
                    addWine(action.name, action.brand, action.style)
                }

                ConsumeWineAction.SubmitConsumedWine -> {
                    submitConsumedWine()
                }
            }
        }
    }

    private suspend fun addWineBrand(name: String) {
        handleResult(
            addWineBrandUseCase(AddBrandRequest(name)),
            { wineBrand ->
                updateState {
                    copy(
                        selectedBrand = wineBrand,
                        selectedWine = null,
                        wines = emptyList()
                    )
                }
                sendEvent(ConsumeWineEvent.AddWineBrandSuccess)
            },
            { error ->
                val errorMsg = resourceHelper.getString(error.stringRes)
                sendEvent(ConsumeWineEvent.AddWineBrandError(errorMsg))
            }
        )
    }

    private suspend fun addWine(name: String, brand: WineBrand, style: WineStyle) {
        handleResult(
            addWineUseCase(AddWineRequest(name, brand, style)),
            { wineId ->
                val newWine = Drink.Wine(
                    wineId = wineId.toInt(),
                    name = name,
                    style = style,
                    brand = brand,
                    wineColor = style.color
                )
                updateState {
                    copy(
                        selectedStyle = style,
                        selectedBrand = brand,
                        selectedWine = newWine
                    )
                }
                sendEvent(ConsumeWineEvent.AddWineSuccess)
            },
            { error ->
                val errorMsg = resourceHelper.getString(error.stringRes)
                sendEvent(ConsumeWineEvent.AddWineError(errorMsg))
            }
        )
    }

    private suspend fun submitConsumedWine() {
        uiState.value.selectedWine?.let {
            handleResult(
                addConsumedDrink(it),
                {
                    sendEvent(ConsumeWineEvent.SubmitConsumedWineSuccess)
                },
                { error ->
                    val errorMsg = resourceHelper.getString(error.stringRes)
                    updateState { copy(errorMsg = errorMsg) }
                    sendEvent(ConsumeWineEvent.SubmitConsumedWineError(errorMsg))
                }
            )
        }
    }


}
