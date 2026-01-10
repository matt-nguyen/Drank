package com.nghianguyen.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nghianguyen.local.model.ConsumedBeerRow
import com.nghianguyen.local.model.ConsumedCocktailRow
import com.nghianguyen.local.model.ConsumedDrinkEntity
import com.nghianguyen.local.model.ConsumedShotRow
import com.nghianguyen.local.model.ConsumedWineRow
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumedDrinkDao {

    @Query("SELECT * FROM consumed_drink WHERE drink_type = 'beer' AND timestamp >= :start AND timestamp <= :end ORDER BY timestamp DESC")
    fun getConsumedBeers(start: String, end: String): Flow<List<ConsumedBeerRow>>


    @Query("SELECT * FROM consumed_drink WHERE drink_type = 'wine' AND timestamp >= :start AND timestamp <= :end ORDER BY timestamp DESC")
    fun getConsumedWines(start: String, end: String): Flow<List<ConsumedWineRow>>

    @Query("SELECT * FROM consumed_drink WHERE drink_type = 'shot' AND timestamp >= :start AND timestamp <= :end ORDER BY timestamp DESC")
    fun getConsumedShots(start: String, end: String): Flow<List<ConsumedShotRow>>

    @Query("SELECT * FROM consumed_drink WHERE drink_type = 'cocktail' AND timestamp >= :start AND timestamp <= :end ORDER BY timestamp DESC")
    fun getConsumedCocktails(start: String, end: String): Flow<List<ConsumedCocktailRow>>

    @Insert
    suspend fun addConsumedDrink(consumedDrinkEntity: ConsumedDrinkEntity): Long
}