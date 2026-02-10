package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Ok
import com.nghianguyen.base.toUiText
import com.nghianguyen.consume.ui.shot.AddShotDialogState
import com.nghianguyen.consume.ui.shot.ShotAddDialogType
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotAction
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotEvent
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotDialogState
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
    addConsumedDrinkUseCase: AddConsumedDrinkUseCase
): ConsumeDrinkViewModel<ConsumeShotDialogState, ConsumeShotAction, ConsumeShotEvent>(addConsumedDrinkUseCase) {
    override fun buildInitialState() = ConsumeShotDialogState(
        liquors = emptyList(),
        shots = emptyList(),
        selectedLiquor = null,
        selectedShot = null,
        addDialogState = null,
        errorMsg = null
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            liquorRepository.getLiquors()
                .onResult(
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
                        getShotsByLiquorUseCase(GetShotsByLiquorRequest(it))
                    } ?: flowOf(Ok(emptyList()))
                }.collect { shotsResult ->
                    shotsResult.onResult(
                        onSuccess = { shots ->
                            updateState { copy(shots = shots) }
                        },
                        onFailure = {}
                    )
                }
        }
    }

    override fun handleAction(action: ConsumeShotAction) {
        Log.d("ConsumeShotViewModel", "handleAction: $action")
        when (action) {
            is ConsumeShotAction.LiquorSelected -> {
                selectLiquor(action.selectedLiquor)
            }
            is ConsumeShotAction.ShotSelected -> {
                selectShot(action.selectedShot)
            }
            ConsumeShotAction.OpenAddShotDialog -> {
                openAddShotDialog()
            }
            ConsumeShotAction.DismissAddDialog -> {
                updateState { copy(addDialogState = null) }
            }
            is ConsumeShotAction.AddShot -> {
                addShot(action.name, action.liquor)
            }
            ConsumeShotAction.SubmitConsumedShot -> {
                submitConsumedShot()
            }
        }
    }

    private fun selectLiquor(liquor: Liquor?) {
        updateState { copy(selectedLiquor = liquor) }
    }

    private fun selectShot(shot: Drink.Shot?) {
        updateState { copy(selectedShot = shot) }
    }

    private fun openAddShotDialog() {
        val addShotDialogState = AddShotDialogState(uiState.value.liquors)
        updateState {
            copy(addDialogState = ShotAddDialogType.AddShot(addShotDialogState))
        }
    }

    private fun addShot(name: String, liquor: Liquor) {
        launch {
            addShotUseCase(AddShotRequest(name, liquor)).onResult(
                onSuccess = { shotId ->
                    updateState {
                        copy(
                            selectedShot = Drink.Shot(shotId.toInt(), name, liquor),
                            selectedLiquor = liquor,
                            addDialogState = null
                        )
                    }
                },
                onFailure = { error ->
                    val addDialogState =
                        when (val currentAddDialogState = uiState.value.addDialogState) {
                            is ShotAddDialogType.AddShot -> {
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

    private fun submitConsumedShot() {
        launch {
            uiState.value.selectedShot?.let {
                addConsumedDrink(it).onResult(
                    onSuccess = { sendEvent(ConsumeShotEvent.SubmitConsumeShotSuccess) },
                    onFailure = { error ->
                        updateState { copy(errorMsg = error.toUiText()) }
                    }
                )
            }
        }
    }

}
