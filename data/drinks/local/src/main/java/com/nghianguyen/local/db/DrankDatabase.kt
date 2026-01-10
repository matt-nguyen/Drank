package com.nghianguyen.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nghianguyen.local.db.daos.BeerDao
import com.nghianguyen.local.db.daos.CocktailDao
import com.nghianguyen.local.db.daos.ConsumedDrinkDao
import com.nghianguyen.local.db.daos.LiquorDao
import com.nghianguyen.local.db.daos.ShotDao
import com.nghianguyen.local.db.daos.WineDao
import com.nghianguyen.local.db.typeconverters.OffsetDateTimeConverter
import com.nghianguyen.local.model.ConsumedDrinkEntity
import com.nghianguyen.local.model.LiquorEntity
import com.nghianguyen.local.model.beer.BeerBrandEntity
import com.nghianguyen.local.model.beer.BeerEntity
import com.nghianguyen.local.model.beer.BeerFullView
import com.nghianguyen.local.model.beer.BeerStyleEntity
import com.nghianguyen.local.model.cocktail.CocktailEntity
import com.nghianguyen.local.model.cocktail.CocktailFullView
import com.nghianguyen.local.model.shot.ShotEntity
import com.nghianguyen.local.model.shot.ShotFullView
import com.nghianguyen.local.model.wine.WineBrandEntity
import com.nghianguyen.local.model.wine.WineEntity
import com.nghianguyen.local.model.wine.WineFullView
import com.nghianguyen.local.model.wine.WineStyleEntity

@Database(
    entities = [
        BeerEntity::class, BeerStyleEntity::class, BeerBrandEntity::class,
        CocktailEntity::class, ConsumedDrinkEntity::class, LiquorEntity::class, ShotEntity::class,
        WineEntity::class, WineBrandEntity::class, WineStyleEntity::class
    ],
    views = [BeerFullView::class, CocktailFullView::class, ShotFullView::class, WineFullView::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    value = [OffsetDateTimeConverter::class]
)
abstract class DrankDatabase : RoomDatabase() {
    abstract fun consumedDrinkDao(): ConsumedDrinkDao
    abstract fun beerDao(): BeerDao
    abstract fun wineDao(): WineDao
    abstract fun liquorDao(): LiquorDao

    abstract fun shotDao(): ShotDao
    abstract fun cocktailDao(): CocktailDao

    companion object {
        @Volatile
        private var INSTANCE: DrankDatabase? = null

        fun getInstance(context: Context): DrankDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DrankDatabase::class.java,
                    "drank.db"
                )
                    .createFromAsset("db/drank_pre.db")
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}