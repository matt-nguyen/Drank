package com.nghianguyen.drinks.usecase.request

import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle

data class AddBeerRequest(val name: String, val beerBrand: BeerBrand, val beerStyle: BeerStyle) {
    init {
        require(name.isNotBlank() && name.isNotEmpty()) {
            "name should not be empty nor blank: $name"
        }
    }
}