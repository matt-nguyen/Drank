package com.nghianguyen.drinks.usecase.request

import com.nghianguyen.drinks.model.Liquor

data class GetCocktailsByLiquorRequest(val liquor: Liquor)
