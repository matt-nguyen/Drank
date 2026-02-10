package com.nghianguyen.consume.viewmodel.cocktail

import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent

sealed interface ConsumeCocktailEvent: ConsumeDrinkEvent {
    object SubmitConsumeCocktailSuccess: ConsumeCocktailEvent
}