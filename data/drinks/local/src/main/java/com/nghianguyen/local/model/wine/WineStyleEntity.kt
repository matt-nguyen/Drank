package com.nghianguyen.local.model.wine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nghianguyen.drinks.model.wine.WineColor
import com.nghianguyen.drinks.model.wine.WineStyle

@Entity(tableName = "wine_style")
data class WineStyleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "wine_color")
    val color: WineColor,
)

fun WineStyleEntity.toDomain() = WineStyle(id = id, name = name, color = color)