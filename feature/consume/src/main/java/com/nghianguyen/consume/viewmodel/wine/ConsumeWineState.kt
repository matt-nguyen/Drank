package com.nghianguyen.consume.viewmodel.wine

import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle

data class ConsumeWineState(
    val wineStyles: List<WineStyle> = emptyList(),
    val wineBrands: List<WineBrand> = emptyList(),
    val wines: List<Drink.Wine> = emptyList(),

    val selectedStyle: WineStyle? = null,
    val selectedBrand: WineBrand? = null,
    val selectedWine: Drink.Wine? = null,

    val errorMsg: String? = null
) : ConsumeDrinkState
