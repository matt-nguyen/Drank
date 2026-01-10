package com.nghianguyen.home.viewmodel.home

import com.nghianguyen.drinks.model.ConsumedDrink
import java.time.LocalDate

data class HomeState(
    val drinksDate: LocalDate,
    val drinksDateText: String,
    val isToday: Boolean,
    val consumedDrinks: List<ConsumedDrink>,
    val isLoading: Boolean
)
