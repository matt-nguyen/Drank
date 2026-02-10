package com.nghianguyen.consume.ui.cocktail

import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.text.UiText

sealed interface CocktailAddDialogType {
    data class AddCocktail(val addDialogState: AddCocktailDialogState): CocktailAddDialogType
}

data class AddCocktailDialogState(
    val liquors: List<Liquor>,
    val errorMsg: UiText? = null
)