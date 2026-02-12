package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.base.viewmodel.BaseViewModel
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase

interface ConsumeDrinkState
interface ConsumeDrinkAction
interface ConsumeDrinkEvent

abstract class ConsumeDrinkViewModel<S : ConsumeDrinkState, A : ConsumeDrinkAction, E : ConsumeDrinkEvent>(
    private val addConsumedDrinkUseCase: AddConsumedDrinkUseCase
) : BaseViewModel<S, A, E>() {
    protected suspend fun addConsumedDrink(drink: Drink): Result<Long, Error> {
        Log.d("ConsumeDrinkViewModel", "addConsumedDrink: $drink")
        return addConsumedDrinkUseCase(drink)
    }
}