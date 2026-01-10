package com.nghianguyen.drinks.model

import java.time.OffsetDateTime

data class ConsumedDrink(
//    val id: Int = 0,
    val id: Int,
    val drink: Drink,
    val timestamp: OffsetDateTime
)