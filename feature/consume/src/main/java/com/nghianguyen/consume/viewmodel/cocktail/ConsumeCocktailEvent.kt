package com.nghianguyen.consume.viewmodel.cocktail

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeCocktailEvent: ConsumeDrinkEvent {
    object AddCocktailSuccess: ConsumeCocktailEvent
    data class AddCocktailError(val errorMsg: String): ConsumeCocktailEvent
    object SubmitConsumeCocktailSuccess: ConsumeCocktailEvent
    data class SubmitConsumeCocktailError(val errorMsg: String): ConsumeCocktailEvent
}