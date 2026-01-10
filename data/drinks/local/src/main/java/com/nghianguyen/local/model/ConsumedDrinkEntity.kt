package com.nghianguyen.local.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.local.model.beer.BeerFullView
import com.nghianguyen.local.model.beer.toDomain
import com.nghianguyen.local.model.cocktail.CocktailFullView
import com.nghianguyen.local.model.cocktail.toDomain
import com.nghianguyen.local.model.shot.ShotFullView
import com.nghianguyen.local.model.shot.toDomain
import com.nghianguyen.local.model.wine.WineFullView
import com.nghianguyen.local.model.wine.toDomain
import java.time.OffsetDateTime

@Entity(tableName = "consumed_drink")
data class ConsumedDrinkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "drink_type")
    val drinkType: String,
    @ColumnInfo(name = "drink_id") // no foreign key since this column links to multiple tables (beer, wine, ...)
    val drinkId: Int = 0,
    @ColumnInfo(name = "timestamp")
    val timestamp: OffsetDateTime
)

data class ConsumedBeerRow(
    @Embedded
    val consumedDrinkEntity: ConsumedDrinkEntity,
    @Relation(parentColumn = "drink_id", entityColumn = "id")
    val beerFullView: BeerFullView
)

data class ConsumedWineRow(
    @Embedded
    val consumedDrinkEntity: ConsumedDrinkEntity,
    @Relation(parentColumn = "drink_id", entityColumn = "id")
    val wineFullView: WineFullView
)

data class ConsumedShotRow(
    @Embedded
    val consumedDrinkEntity: ConsumedDrinkEntity,
    @Relation(parentColumn = "drink_id", entityColumn = "id")
    val shotFullView: ShotFullView
)

data class ConsumedCocktailRow(
    @Embedded
    val consumedDrinkEntity: ConsumedDrinkEntity,
    @Relation(parentColumn = "drink_id", entityColumn = "id")
    val cocktailFullView: CocktailFullView
)

fun ConsumedBeerRow.toDomain(): ConsumedDrink {
    return ConsumedDrink(
        id = consumedDrinkEntity.id,
        drink = beerFullView.toDomain(),
        timestamp = consumedDrinkEntity.timestamp
    )
}

fun ConsumedWineRow.toDomain(): ConsumedDrink {
    return ConsumedDrink(
        id = consumedDrinkEntity.id,
        drink = wineFullView.toDomain(),
        timestamp = consumedDrinkEntity.timestamp
    )
}

fun ConsumedShotRow.toDomain(): ConsumedDrink {
    return ConsumedDrink(
        id = consumedDrinkEntity.id,
        drink = shotFullView.toDomain(),
        timestamp = consumedDrinkEntity.timestamp
    )
}

fun ConsumedCocktailRow.toDomain(): ConsumedDrink {
    return ConsumedDrink(
        id = consumedDrinkEntity.id,
        drink = cocktailFullView.toDomain(),
        timestamp = consumedDrinkEntity.timestamp
    )
}
