package com.nghianguyen.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nghianguyen.local.model.cocktail.CocktailEntity
import com.nghianguyen.local.model.cocktail.CocktailFullView
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Query(
        "SELECT cocktail.id, cocktail.name, cocktail.liquor_id AS liquorId, liquor.name AS liquorName " +
                "FROM cocktail " +
                "LEFT JOIN liquor ON cocktail.liquor_id = liquor.id " +
                "WHERE liquor_id = :liquorId"
    )
    fun getCocktailsByLiquor(liquorId: Int): Flow<List<CocktailFullView>>

    @Insert
    suspend fun addCocktail(cocktailEntity: CocktailEntity): Long
}