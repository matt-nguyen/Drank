package com.nghianguyen.local.model.cocktail

import androidx.room.DatabaseView
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor

@DatabaseView(
    "SELECT cocktail.id, cocktail.name, cocktail.liquor_id AS liquorId, liquor.name AS liquorName " +
            "FROM cocktail " +
            "LEFT JOIN liquor ON cocktail.liquor_id = liquor.id"
)
data class CocktailFullView(
    val id: Int,
    val name: String,
    val liquorId: Int,
    val liquorName: String
)

fun CocktailFullView.toDomain(): Drink.Cocktail {
    return Drink.Cocktail(
        cocktailId = id,
        name = name,
        liquor = Liquor(id = liquorId, name = liquorName)
    )
}