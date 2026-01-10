package com.nghianguyen.local.model.beer

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.nghianguyen.drinks.model.beer.BeerStyle

@Entity(
    tableName = "beer_style",
    foreignKeys = [
        ForeignKey(
            entity = BeerStyleEntity::class,
            parentColumns = ["id"],
            childColumns = ["parent_style_id"]
        )
    ]
)
data class BeerStyleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "parent_style_id", index = true)
    val parentStyleId: Int?
)



data class BeerStyleWithSubs(
    @Embedded
    val beerStyleEntity: BeerStyleEntity,
    @Relation(parentColumn = "id", entityColumn = "parent_style_id")
    val subBeerStyles: List<BeerStyleEntity>
)

fun BeerStyleEntity.toDomain() = BeerStyle(id = id, name = name)

fun BeerStyleWithSubs.toDomain() =
    BeerStyle(
        id = beerStyleEntity.id,
        name = beerStyleEntity.name,
        subBeerStyles = subBeerStyles.map { it.toDomain() }
    )