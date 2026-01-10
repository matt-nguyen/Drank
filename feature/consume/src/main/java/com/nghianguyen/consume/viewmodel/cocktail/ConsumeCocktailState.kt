package com.nghianguyen.consume.viewmodel.cocktail

import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor

data class ConsumeCocktailState(
    val liquors: List<Liquor>,
    val cocktails: List<Drink.Cocktail>,

    val selectedLiquor: Liquor?,
    val selectedCocktail: Drink.Cocktail?,

    val errorMsg: String?
): ConsumeDrinkState
