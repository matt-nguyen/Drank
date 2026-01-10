package com.nghianguyen.drinks.model.beer

data class BeerStyle(
    val id: Int,
    val name: String,
    val subBeerStyles: List<BeerStyle>? = null
)