package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.ResourcesHelper
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotAction
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotEvent
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.repository.LiquorRepository
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.AddShotUseCase
import com.nghianguyen.drinks.usecase.GetShotsByLiquorUseCase
import com.nghianguyen.drinks.usecase.request.AddShotRequest
import com.nghianguyen.drinks.usecase.request.GetShotsByLiquorRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ConsumeShotViewModel @Inject constructor(
    private val liquorRepository: LiquorRepository,
    private val getShotsByLiquorUseCase: GetShotsByLiquorUseCase,
    private val addShotUseCase: AddShotUseCase,
    private val resourceHelper: ResourcesHelper,
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
): ConsumeDrinkViewModel<ConsumeShotState, ConsumeShotAction, ConsumeShotEvent>(addConsumedDrinkUseCase) {
    override fun buildInitialState() = ConsumeShotState(
        liquors = emptyList(),
        shots = emptyList(),
        selectedLiquor = null,
        selectedShot = null,
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
                        getShotsByLiquorUseCase(GetShotsByLiquorRequest(selectedLiquor))
                    } else {
                        flowOf(Ok(emptyList()))
                    }
                }.collect { shotsResult ->
                    handleResult(
                        shotsResult,
                        { shots ->
                            updateState { copy(shots = shots) }
                        },
                        { }
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeShotAction) {
        Log.d("ConsumeShotViewModel", "handleAction: $action")
        launch {
            when (action) {
                is ConsumeShotAction.LiquorSelected -> {
                    updateState { copy(selectedLiquor = action.selectedLiquor) }
                }
                is ConsumeShotAction.ShotSelected -> {
                    updateState { copy(selectedShot = action.selectedShot) }
                }
                is ConsumeShotAction.AddShot -> {
                    addShot(action.name, action.liquor)
                }
                ConsumeShotAction.SubmitConsumedShot -> {
                    submitConsumedShot()
                }
            }
        }
    }

    private suspend fun addShot(name: String, liquor: Liquor) {
        handleResult(
            addShotUseCase(AddShotRequest(name, liquor)),
            { shotId ->
                updateState {
                    copy(
                        selectedShot = Drink.Shot(shotId.toInt(), name, liquor),
                        selectedLiquor = liquor
                    )
                }
                sendEvent(ConsumeShotEvent.AddShotSuccess)
            },
            { error ->
                val errorMsg = resourceHelper.getString(error.stringRes)
                sendEvent(ConsumeShotEvent.AddShotError(errorMsg))
            }
        )
    }

    private suspend fun submitConsumedShot() {
        uiState.value.selectedShot?.let {
            handleResult(
                addConsumedDrink(it),
                {
                    sendEvent(ConsumeShotEvent.SubmitConsumeShotSuccess)
                },
                { error ->
                    val errorMsg = resourceHelper.getString(error.stringRes)
                    updateState { copy(errorMsg = errorMsg) }
                    sendEvent(ConsumeShotEvent.SubmitConsumeShotError(errorMsg))
                }
            )
        }
    }

}
