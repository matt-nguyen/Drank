package com.nghianguyen.drinks.usecase.request

import com.nghianguyen.drinks.model.Liquor

data class AddCocktailRequest(val name: String, val liquor: Liquor) {
    init {
        require(name.isNotBlank() && name.isNotEmpty()) {
            "name should not be empty nor blank: $name"
        }
    }
}
