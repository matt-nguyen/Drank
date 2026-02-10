package com.nghianguyen.consume.viewmodel.wine

import com.nghianguyen.consume.viewmodel.ConsumeDrinkAction
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle

sealed interface ConsumeWineAction : ConsumeDrinkAction {
    data class StyleSelected(val selectedStyle: WineStyle?) : ConsumeWineAction
    data class BrandSelected(val selectedBrand: WineBrand?) : ConsumeWineAction
    data class WineSelected(val selectedWine: Drink.Wine?) : ConsumeWineAction

    data object OpenAddBrandDialog: ConsumeWineAction
    data object OpenAddWineDialog: ConsumeWineAction
    data object DismissAddDialog: ConsumeWineAction

    data class AddBrand(val brandName: String) : ConsumeWineAction
    data class AddWine(val name: String, val brand: WineBrand, val style: WineStyle) :
        ConsumeWineAction

    object SubmitConsumedWine : ConsumeWineAction
}
