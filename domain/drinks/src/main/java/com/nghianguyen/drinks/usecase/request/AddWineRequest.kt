package com.nghianguyen.drinks.usecase.request

import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle

data class AddWineRequest(val name: String, val wineBrand: WineBrand, val wineStyle: WineStyle) {
    init {
        require(name.isNotBlank() && name.isNotEmpty()) {
            "name should not be empty nor blank: $name"
        }
    }
}