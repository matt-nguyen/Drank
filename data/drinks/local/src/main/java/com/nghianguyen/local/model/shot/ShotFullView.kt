package com.nghianguyen.local.model.shot

import androidx.room.DatabaseView
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor

@DatabaseView(
    "SELECT shot.id, shot.name, shot.liquor_id AS liquorId, liquor.name AS liquorName " +
            "FROM shot " +
            "LEFT JOIN liquor ON shot.liquor_id = liquor.id"
)
data class ShotFullView(
    val id: Int,
    val name: String,
    val liquorId: Int,
    val liquorName: String
)

fun ShotFullView.toDomain(): Drink.Shot {
    return Drink.Shot(
        shotId = id,
        name = name,
        liquor = Liquor(id = liquorId, name = liquorName)
    )
}