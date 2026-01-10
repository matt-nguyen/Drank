package com.nghianguyen.consume.viewmodel.wine

import com.nghianguyen.consume.viewmodel.ConsumeDrinkAction
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle

sealed interface ConsumeWineAction : ConsumeDrinkAction {
    data class WineStyleSelected(val selectedStyle: WineStyle?) : ConsumeWineAction
    data class WineBrandSelected(val selectedBrand: WineBrand?) : ConsumeWineAction
    data class WineSelected(val selectedWine: Drink.Wine?) : ConsumeWineAction
    data class AddWineBrand(val brandName: String) : ConsumeWineAction
    data class AddWine(val name: String, val brand: WineBrand, val style: WineStyle) :
        ConsumeWineAction

    object SubmitConsumedWine : ConsumeWineAction
}
