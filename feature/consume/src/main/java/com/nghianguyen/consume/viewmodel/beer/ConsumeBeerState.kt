package com.nghianguyen.consume.viewmodel.beer

import com.nghianguyen.consume.viewmodel.ConsumeDrinkState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle

data class ConsumeBeerState(
    val beerStyles: List<BeerStyle>,
    val beerBrands: List<BeerBrand>,
    val beers: List<Drink.Beer>,

    val selectedStyle: BeerStyle?,
    val selectedBrand: BeerBrand?,
    val selectedBeer: Drink.Beer?,

    val errorMsg: String?
) : ConsumeDrinkState
