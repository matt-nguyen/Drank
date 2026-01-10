package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.ResourcesHelper
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailAction
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailEvent
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.repository.LiquorRepository
import com.nghianguyen.drinks.usecase.AddCocktailUseCase
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.GetCocktailsByLiquorUseCase
import com.nghianguyen.drinks.usecase.request.AddCocktailRequest
import com.nghianguyen.drinks.usecase.request.GetCocktailsByLiquorRequest
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
    private val resourceHelper: ResourcesHelper,
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
): ConsumeDrinkViewModel<ConsumeCocktailState, ConsumeCocktailAction, ConsumeCocktailEvent>(addConsumedDrinkUseCase) {
    override fun buildInitialState() = ConsumeCocktailState(
        liquors = emptyList(),
        cocktails = emptyList(),
        selectedLiquor = null,
        selectedCocktail = null,
        errorMsg = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            handleResult(
                liquorRepository.getLiquors(),
                { liquors ->
                    updateState { copy(liquors = liquors) }
                },
                { }
            )
        }

        launch {
            uiState.map { it.selectedLiquor }
                .flatMapLatest { selectedLiquor ->
                    if (selectedLiquor != null) {
                        getCocktailsByLiquorUseCase(GetCocktailsByLiquorRequest(selectedLiquor))
                    } else {
                        flowOf(Ok(emptyList()))
                    }
                }.collect { cocktailsResult ->
                    handleResult(
                        cocktailsResult,
                        { cocktails ->
                            updateState { copy(cocktails = cocktails) }
                        },
                        { }
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeCocktailAction) {
        Log.d("ConsumeCocktailViewModel", "handleAction: $action")
        launch {
            when (action) {
                is ConsumeCocktailAction.LiquorSelected -> {
                    updateState { copy(selectedLiquor = action.selectedLiquor) }
                }
                is ConsumeCocktailAction.CocktailSelected -> {
                    updateState { copy(selectedCocktail = action.selectedCocktail) }
                }
                is ConsumeCocktailAction.AddCocktail -> {
                    addCocktail(action.name, action.liquor)
                }
                ConsumeCocktailAction.SubmitConsumedCocktail -> {
                    submitConsumedCocktail()
                }
            }
        }
    }

    private suspend fun addCocktail(name: String, liquor: Liquor) {
        handleResult(
            addCocktailUseCase(AddCocktailRequest(name, liquor)),
            { cocktailId ->
                updateState {
                    copy(
                        selectedCocktail = Drink.Cocktail(cocktailId.toInt(), name, liquor),
                        selectedLiquor = liquor
                    )
                }
                sendEvent(ConsumeCocktailEvent.AddCocktailSuccess)
            },
            { error ->
                val errorMsg = resourceHelper.getString(error.stringRes)
                sendEvent(ConsumeCocktailEvent.AddCocktailError(errorMsg))
            }
        )
    }

    private suspend fun submitConsumedCocktail() {
        uiState.value.selectedCocktail?.let {
            handleResult(
                addConsumedDrink(it),
                {
                    sendEvent(ConsumeCocktailEvent.SubmitConsumeCocktailSuccess)
                },
                { error ->
                    val errorMsg = resourceHelper.getString(error.stringRes)
                    updateState { copy(errorMsg = errorMsg) }
                    sendEvent(ConsumeCocktailEvent.SubmitConsumeCocktailError(errorMsg))
                }
            )
        }
    }

}
