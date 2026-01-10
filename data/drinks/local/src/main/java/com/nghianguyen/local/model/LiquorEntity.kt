package com.nghianguyen.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nghianguyen.drinks.model.Liquor

@Entity(
    tableName = "liquor",
    indices = [Index(name = "liquor_name_unique", value = ["name"], unique = true)]
)
data class LiquorEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
)

fun LiquorEntity.toDomain() = Liquor(id = id, name = name)