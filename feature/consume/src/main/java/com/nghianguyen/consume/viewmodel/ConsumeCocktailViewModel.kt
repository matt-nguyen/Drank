package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.toUiText
import com.nghianguyen.consume.ui.cocktail.AddCocktailDialogState
import com.nghianguyen.consume.ui.cocktail.CocktailAddDialogType
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailAction
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailDialogState
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailEvent
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.repository.LiquorRepository
import com.nghianguyen.drinks.usecase.AddCocktailUseCase
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.GetCocktailsByLiquorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ConsumeCocktailViewModel @Inject constructor(
    private val liquorRepository: LiquorRepository,
    private val getCocktailsByLiquorUseCase: GetCocktailsByLiquorUseCase,
    private val addCocktailUseCase: AddCocktailUseCase,
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
): ConsumeDrinkViewModel<ConsumeCocktailDialogState, ConsumeCocktailAction, ConsumeCocktailEvent>(addConsumedDrinkUseCase) {
    override fun buildInitialState() = ConsumeCocktailDialogState(
        liquors = emptyList(),
        cocktails = emptyList(),
        selectedLiquor = null,
        selectedCocktail = null,
        addDialogState = null,
        errorMsg = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            liquorRepository.getLiquors().onResult(
                onSuccess = { liquors ->
                    updateState { copy(liquors = liquors) }
                },
                onFailure = {}
            )
        }

        launch {
            uiState.map { it.selectedLiquor }
                .flatMapLatest { liquor ->
                    liquor?.let {
                        getCocktailsByLiquorUseCase(it)
                    } ?: flowOf(Ok(emptyList()))
                }.collect { cocktailsResult ->
                    cocktailsResult.onResult(
                        onSuccess = { cocktails ->
                            updateState { copy(cocktails = cocktails) }
                        },
                        onFailure = {}
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeCocktailAction) {
        Log.d("ConsumeCocktailViewModel", "handleAction: $action")
        when (action) {
            is ConsumeCocktailAction.LiquorSelected -> {
                selectLiquor(action.selectedLiquor)
            }
            is ConsumeCocktailAction.CocktailSelected -> {
                selectCocktail(action.selectedCocktail)
            }
            ConsumeCocktailAction.OpenAddCocktailDialog -> {
                openAddCocktailDialog()
            }
            ConsumeCocktailAction.DismissAddDialog -> {
                updateState { copy(addDialogState = null) }
            }
            is ConsumeCocktailAction.AddCocktail -> {
                addCocktail(action.name, action.liquor)
            }
            ConsumeCocktailAction.SubmitConsumedCocktail -> {
                submitConsumedCocktail()
            }
        }
    }

    private fun selectLiquor(liquor: Liquor?) {
        updateState { copy(selectedLiquor = liquor) }
    }

    private fun selectCocktail(cocktail: Drink.Cocktail?) {
        updateState { copy(selectedCocktail = cocktail) }
    }

    private fun openAddCocktailDialog() {
        val addCocktailDialogState = AddCocktailDialogState(
            liquors = uiState.value.liquors
        )
        updateState {
            copy(
                addDialogState =
                    CocktailAddDialogType.AddCocktail(addCocktailDialogState)
            )
        }
    }
    private fun addCocktail(name: String, liquor: Liquor) {
        launch {
            addCocktailUseCase(name, liquor).onResult(
                onSuccess = { cocktailId ->
                    updateState {
                        copy(
                            selectedCocktail = Drink.Cocktail(cocktailId.toInt(), name, liquor),
                            selectedLiquor = liquor,
                            addDialogState = null
                        )
                    }
                },
                onFailure = { error ->
                    val addDialogState =
                        when (val currentAddDialogState = uiState.value.addDialogState) {
                            is CocktailAddDialogType.AddCocktail -> {
                                currentAddDialogState.copy(
                                    addDialogState = currentAddDialogState.addDialogState.copy(
                                        errorMsg = error.toUiText()
                                    )
                                )
                            }
                            null -> currentAddDialogState
                        }
                    updateState { copy(addDialogState = addDialogState) }
                }
            )
        }
    }

    private fun submitConsumedCocktail() {
        launch {
            uiState.value.selectedCocktail?.let {
                addConsumedDrink(it).onResult(
                    onSuccess = {
                        sendEvent(ConsumeCocktailEvent.SubmitConsumeCocktailSuccess)
                    },
                    onFailure = { error ->
                        updateState { copy(errorMsg = error.toUiText()) }
                    }
                )
            }
        }
    }

}
