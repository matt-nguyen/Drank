package com.nghianguyen.consume.ui.beer

import com.nghianguyen.consume.ui.AddBrandDialogState
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.text.UiText

sealed interface BeerAddDialogType {
    data class AddBeer(val addDialogState: AddBeerDialogState): BeerAddDialogType
    data class AddBrand(val addDialogState: AddBrandDialogState): BeerAddDialogType
}

data class AddBeerDialogState(
    val beerStyles: List<BeerStyle>,
    val beerBrands: List<BeerBrand>,
    val errorMsg: UiText? = null
)