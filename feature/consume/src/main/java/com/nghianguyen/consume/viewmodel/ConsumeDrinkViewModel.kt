package com.nghianguyen.consume.viewmodel

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.base.viewmodel.BaseViewModel
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.usecase.AddConsumedDrinkUseCase
import com.nghianguyen.drinks.usecase.request.AddConsumedDrinkRequest

interface ConsumeDrinkState
interface ConsumeDrinkAction
interface ConsumeDrinkEvent

abstract class ConsumeDrinkViewModel<S : ConsumeDrinkState, A : ConsumeDrinkAction, E : ConsumeDrinkEvent>(
    private val addConsumedDrinkUseCase: AddConsumedDrinkUseCase
) : BaseViewModel<S, A, E>() {

    protected suspend fun addConsumedDrink(drink: Drink): Result<Long, LocalDataError> {
        Log.d("ConsumeDrinkViewModel", "addConsumedDrink: $drink")
        return addConsumedDrinkUseCase(AddConsumedDrinkRequest(drink))
    }

}