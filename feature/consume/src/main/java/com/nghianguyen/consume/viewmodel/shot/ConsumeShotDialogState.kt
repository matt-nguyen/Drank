package com.nghianguyen.consume.viewmodel.shot

import com.nghianguyen.consume.ui.shot.ShotAddDialogType
import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.text.UiText

data class ConsumeShotDialogState(
    val liquors: List<Liquor>,
    val shots: List<Drink.Shot>,

    val selectedLiquor: Liquor?,
    val selectedShot: Drink.Shot?,

    val addDialogState: ShotAddDialogType?,
    val errorMsg: UiText?
): ConsumeDrinkState
