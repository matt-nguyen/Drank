package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.model.LocalDataError
import kotlinx.coroutines.flow.Flow

interface CocktailLocalDataSource {

    /**
     * Get a list of [Drink.Cocktail] by liquor.
     *
     * @param [Int] liquorId - id from a [Liquor]
     * @return [Flow<Result<List<Drink.Cocktail>, LocalDataError>>]
     */
    fun getCocktailsByLiquor(liquorId: Int): Flow<Result<List<Drink.Cocktail>, LocalDataError>>

    /**
     * Save a new [Drink.Cocktail] locally.
     *
     * @param [String] name - cocktail name
     * @param [Int] liquorId - id from a [Liquor]
     * @return [Result<Long, LocalDataError>] - id of the new [Drink.Cocktail]
     */
    suspend fun addCocktail(name: String, liquorId: Int): Result<Long, LocalDataError>
}