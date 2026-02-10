package com.nghianguyen.consume.viewmodel.beer

import com.nghianguyen.consume.ui.beer.BeerAddDialogType
import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.text.UiText

data class ConsumeBeerDialogState(
    val beerStyles: List<BeerStyle>,
    val beerBrands: List<BeerBrand>,
    val beers: List<Drink.Beer>,

    val selectedStyle: BeerStyle?,
    val selectedBrand: BeerBrand?,
    val selectedBeer: Drink.Beer?,

    val addDialogState: BeerAddDialogType?,
    val errorMsg: UiText?
) : ConsumeDrinkState
