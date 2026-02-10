package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.toUiText
import com.nghianguyen.consume.ui.AddBrandDialogState
import com.nghianguyen.consume.ui.wine.AddWineDialogState
import com.nghianguyen.consume.ui.wine.WineAddDialogType
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineAction
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineDialogState
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineEvent
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
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
) : ConsumeDrinkViewModel<ConsumeWineDialogState, ConsumeWineAction, ConsumeWineEvent>(
    addConsumedDrinkUseCase
) {
    override fun buildInitialState() = ConsumeWineDialogState(
        wineStyles = emptyList(),
        wineBrands = emptyList(),
        wines = emptyList(),
        selectedStyle = null,
        selectedBrand = null,
        selectedWine = null,
        addDialogState = null,
        errorMsg = null
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            wineRepository.getWineStyles().onResult(
                onSuccess = { wineStyles ->
                    updateState { copy(wineStyles = wineStyles) }
                },
                onFailure = {}
            )
        }
        launch {
            wineRepository.getWineBrands()
                .collect { wineBrandsResult ->
                    wineBrandsResult.onResult(
                        onSuccess = { wineBrands ->
                            updateState { copy(wineBrands = wineBrands) }
                        },
                        onFailure = {}
                    )
                }
        }

        launch {
            uiState.map { it.selectedBrand }
                .distinctUntilChanged()
                .flatMapLatest { brand ->
                    brand?.let {
                        getWinesByBrandUseCase(GetWinesByBrandRequest(it))
                    } ?:flowOf(Ok(emptyList()))
                }.collect { winesByBrandResult ->
                    winesByBrandResult.onResult(
                        onSuccess = { wines ->
                            val selectedWine = uiState.value.selectedWine
                            val newSelectedWine =
                                selectedWine?.takeIf { selected ->
                                    wines.any { selected.wineId == it.wineId }
                                }
                                if (wines.map { it.wineId }
                                        .contains(selectedWine?.wineId)) selectedWine
                                else null
                            updateState { copy(wines = wines, selectedWine = newSelectedWine) }
                        },
                        onFailure = {}
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeWineAction) {
        Log.d("ConsumeWineViewModel", "handleAction: $action")
        when (action) {
            is ConsumeWineAction.BrandSelected -> {
                selectBrand(action.selectedBrand)
            }
            is ConsumeWineAction.StyleSelected -> {
                selectStyle(action.selectedStyle)
            }
            is ConsumeWineAction.WineSelected -> {
                selectWine(action.selectedWine)
            }
            ConsumeWineAction.OpenAddBrandDialog -> {
                openAddBrandDialog()
            }
            ConsumeWineAction.OpenAddWineDialog -> {
                openAddWineDialog()
            }
            ConsumeWineAction.DismissAddDialog -> {
                updateState { copy(addDialogState = null) }
            }
            is ConsumeWineAction.AddBrand -> {
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

    private fun selectStyle(style: WineStyle?) {
        updateState { copy(selectedStyle = style, selectedWine = null) }
    }

    private fun selectBrand(brand: WineBrand?) {
        updateState { copy(selectedBrand = brand, selectedWine = null) }
    }

    private fun selectWine(wine: Drink.Wine?) {
        updateState {
            copy(
                selectedWine = wine,
                selectedStyle = wine?.style
            )
        }
    }

    private fun openAddWineDialog() {
        val uiState = uiState.value
        val addWineDialogState = AddWineDialogState(
            wineStyles = uiState.wineStyles,
            wineBrands = uiState.wineBrands
        )
        updateState {
            copy(addDialogState = WineAddDialogType.AddWine(addWineDialogState))
        }
    }

    private fun openAddBrandDialog() {
        updateState {
            copy(addDialogState = WineAddDialogType.AddBrand(AddBrandDialogState()))
        }
    }

    private fun addWineBrand(name: String) {
        launch {
            addWineBrandUseCase(AddBrandRequest(name)).onResult(
                onSuccess = { wineBrand ->
                    updateState {
                        copy(
                            selectedBrand = wineBrand,
                            selectedWine = null,
                            wines = emptyList(),
                            addDialogState = null
                        )
                    }
                },
                onFailure = { error ->
                    val addDialogState =
                        when (val currentAddDialogState = uiState.value.addDialogState) {
                            is WineAddDialogType.AddBrand -> {
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

    private fun addWine(name: String, brand: WineBrand, style: WineStyle) {
        launch {
            addWineUseCase(AddWineRequest(name, brand, style))
                .onResult(
                    onSuccess = { wineId ->
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
                                selectedWine = newWine,
                                addDialogState = null
                            )
                        }
                    },
                    onFailure = { error ->
                        val addDialogState =
                            when (val currentAddDialogState = uiState.value.addDialogState) {
                                is WineAddDialogType.AddWine -> {
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

    private fun submitConsumedWine() {
        launch {
            uiState.value.selectedWine?.let {
                addConsumedDrink(it).onResult(
                    onSuccess = {
                        sendEvent(ConsumeWineEvent.SubmitConsumedWineSuccess)
                    },
                    onFailure = { error ->
                        updateState { copy(errorMsg = error.toUiText()) }
                    }
                )
            }
        }
    }


}
