package com.nghianguyen.consume.viewmodel.beer

import com.nghianguyen.consume.viewmodel.ConsumeDrinkAction
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle

sealed interface ConsumeBeerAction : ConsumeDrinkAction {
    data class BeerStyleSelected(val selectedStyle: BeerStyle?) : ConsumeBeerAction
    data class BeerBrandSelected(val selectedBrand: BeerBrand?) : ConsumeBeerAction
    data class BeerSelected(val selectedBeer: Drink.Beer?) : ConsumeBeerAction
    data class AddBeerBrand(val brandName: String) : ConsumeBeerAction
    data class AddBeer(
        val beerStyle: BeerStyle,
        val beerBrand: BeerBrand,
        val name: String
    ) : ConsumeBeerAction

    object SubmitConsumedBeer : ConsumeBeerAction
}
