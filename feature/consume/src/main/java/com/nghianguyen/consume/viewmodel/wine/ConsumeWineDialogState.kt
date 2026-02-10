package com.nghianguyen.consume.viewmodel.wine

import com.nghianguyen.consume.ui.wine.WineAddDialogType
import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import com.nghianguyen.text.UiText

data class ConsumeWineDialogState(
    val wineStyles: List<WineStyle>,
    val wineBrands: List<WineBrand>,
    val wines: List<Drink.Wine>,

    val selectedStyle: WineStyle?,
    val selectedBrand: WineBrand?,
    val selectedWine: Drink.Wine?,

    val addDialogState: WineAddDialogType?,
    val errorMsg: UiText?
) : ConsumeDrinkState
