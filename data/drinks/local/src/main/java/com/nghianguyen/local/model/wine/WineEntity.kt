package com.nghianguyen.local.model.wine

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "wine",
    indices = [
        Index(name = "wine_brand_and_name", value = ["brand_id", "name"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = WineStyleEntity::class,
            parentColumns = ["id"],
            childColumns = ["style_id"]
        )
    ]
)
data class WineEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "brand_id")
    val brandId: Int?,
    @ColumnInfo(name = "style_id", index = true)
    val styleId: Int?
)
