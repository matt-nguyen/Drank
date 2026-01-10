package com.nghianguyen.consume.viewmodel.cocktail

import com.nghianguyen.consume.viewmodel.ConsumeDrinkAction
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor

sealed interface ConsumeCocktailAction: ConsumeDrinkAction {
    data class LiquorSelected(val selectedLiquor: Liquor?): ConsumeCocktailAction
    data class CocktailSelected(val selectedCocktail: Drink.Cocktail?): ConsumeCocktailAction
    data class AddCocktail(val name: String, val liquor: Liquor): ConsumeCocktailAction
    object SubmitConsumedCocktail: ConsumeCocktailAction
}
