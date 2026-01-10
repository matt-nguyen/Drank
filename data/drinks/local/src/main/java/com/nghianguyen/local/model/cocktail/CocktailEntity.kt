package com.nghianguyen.local.model.cocktail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nghianguyen.local.model.LiquorEntity

@Entity(
    tableName = "cocktail",
    foreignKeys = [
        ForeignKey(
            entity = LiquorEntity::class,
            parentColumns = ["id"],
            childColumns = ["liquor_id"]
        )
    ],
    indices = [Index(name = "cocktail_name_unique", value = ["name"], unique = true)]
)
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "liquor_id", index = true)
    val liquorId: Int?,
)
