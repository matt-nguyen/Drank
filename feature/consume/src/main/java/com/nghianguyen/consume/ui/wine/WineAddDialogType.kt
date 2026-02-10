package com.nghianguyen.consume.ui.wine

import com.nghianguyen.consume.ui.AddBrandDialogState
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import com.nghianguyen.text.UiText

sealed interface WineAddDialogType {
    data class AddWine(val addDialogState: AddWineDialogState): WineAddDialogType
    data class AddBrand(val addDialogState: AddBrandDialogState): WineAddDialogType
}

data class AddWineDialogState(
    val wineStyles: List<WineStyle>,
    val wineBrands: List<WineBrand>,
    val errorMsg: UiText? = null
)