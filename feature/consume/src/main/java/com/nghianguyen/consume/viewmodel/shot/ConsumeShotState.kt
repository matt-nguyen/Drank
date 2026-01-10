package com.nghianguyen.consume.viewmodel.shot

import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor

data class ConsumeShotState(
    val liquors: List<Liquor>,
    val shots: List<Drink.Shot>,

    val selectedLiquor: Liquor?,
    val selectedShot: Drink.Shot?,

    val errorMsg: String?
): ConsumeDrinkState
