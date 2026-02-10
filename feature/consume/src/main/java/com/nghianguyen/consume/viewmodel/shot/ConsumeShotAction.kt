package com.nghianguyen.consume.viewmodel.shot

import com.nghianguyen.consume.viewmodel.ConsumeDrinkAction
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor

sealed interface ConsumeShotAction: ConsumeDrinkAction {
    data class LiquorSelected(val selectedLiquor: Liquor?): ConsumeShotAction
    data class ShotSelected(val selectedShot: Drink.Shot?): ConsumeShotAction

    data object OpenAddShotDialog: ConsumeShotAction
    data object DismissAddDialog: ConsumeShotAction

    data class AddShot(val name: String, val liquor: Liquor): ConsumeShotAction

    object SubmitConsumedShot: ConsumeShotAction
}
