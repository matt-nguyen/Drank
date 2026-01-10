package com.nghianguyen.drinks.model

import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineColor
import com.nghianguyen.drinks.model.wine.WineStyle

sealed class Drink(val id: Int) {
    data class Beer(
        val beerId: Int,
        val name: String,
        val brand: BeerBrand?,
        val style: BeerStyle?
    ) : Drink(beerId)
    data class Wine(
        val wineId: Int,
        val name: String,
        val brand: WineBrand?,
        val style: WineStyle?,
        val wineColor: WineColor
    ) : Drink(wineId)
    data class Shot(
        val shotId: Int,
        val name: String,
        val liquor: Liquor
    ) : Drink(shotId)
    data class Cocktail(
        val cocktailId: Int,
        val name: String,
        val liquor: Liquor
    ) : Drink(cocktailId)
}