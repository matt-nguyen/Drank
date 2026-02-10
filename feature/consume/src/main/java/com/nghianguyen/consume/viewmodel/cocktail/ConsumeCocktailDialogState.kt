package com.nghianguyen.consume.viewmodel.cocktail

import com.nghianguyen.consume.ui.cocktail.CocktailAddDialogType
import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.text.UiText

data class ConsumeCocktailDialogState(
    val liquors: List<Liquor>,
    val cocktails: List<Drink.Cocktail>,

    val selectedLiquor: Liquor?,
    val selectedCocktail: Drink.Cocktail?,

    val addDialogState: CocktailAddDialogType?,
    val errorMsg: UiText?
): ConsumeDrinkState
