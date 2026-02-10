package com.nghianguyen.consume.ui.shot

import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.text.UiText

sealed interface ShotAddDialogType {
    data class AddShot(val addDialogState: AddShotDialogState): ShotAddDialogType
}

data class AddShotDialogState(
    val liquors: List<Liquor>,
    val errorMsg: UiText? = null
)