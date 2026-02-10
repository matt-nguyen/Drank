package com.nghianguyen.consume.viewmodel.beer

import com.nghianguyen.consume.viewmodel.ConsumeDrinkAction
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle

sealed interface ConsumeBeerAction : ConsumeDrinkAction {
    data class StyleSelected(val selectedStyle: BeerStyle?) : ConsumeBeerAction
    data class BrandSelected(val selectedBrand: BeerBrand?) : ConsumeBeerAction
    data class BeerSelected(val selectedBeer: Drink.Beer?) : ConsumeBeerAction

    data object OpenAddBrandDialog: ConsumeBeerAction
    data object OpenAddBeerDialog: ConsumeBeerAction
    data object DismissAddDialog: ConsumeBeerAction

    data class AddBrand(val brandName: String) : ConsumeBeerAction
    data class AddBeer(
        val beerStyle: BeerStyle,
        val beerBrand: BeerBrand,
        val name: String
    ) : ConsumeBeerAction
    object SubmitConsumedBeer : ConsumeBeerAction
}
