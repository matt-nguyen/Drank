package com.nghianguyen.local.model.beer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nghianguyen.drinks.model.beer.BeerBrand

@Entity(
    tableName = "beer_brand",
    indices = [Index(name = "beer_brand_unique", value = ["name"], unique = true)]
)
data class BeerBrandEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String
)
fun BeerBrandEntity.toDomain() = BeerBrand(id = id, name = name)
