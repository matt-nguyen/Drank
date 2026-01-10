package com.nghianguyen.local.model.wine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nghianguyen.drinks.model.wine.WineBrand

@Entity(
    tableName = "wine_brand",
    indices = [Index(name = "wine_brand_unique", value = ["name"], unique = true)]
)
data class WineBrandEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String
)

fun WineBrandEntity.toDomain() = WineBrand(id = id, name = name)