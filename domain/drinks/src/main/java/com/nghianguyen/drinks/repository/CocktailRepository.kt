package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.Liquor
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {

    /**
     * Get a list of [Drink.Cocktail] by liquor.
     *
     * @param [Int] liquorId - id from a [Liquor]
     * @return [Flow<Result<List<Drink.Cocktail>, Error>>]
     */
    fun getCocktailsByLiquor(liquorId: Int): Flow<Result<List<Drink.Cocktail>, Error>>

    /**
     * Save a new [Drink.Cocktail].
     *
     * @param [String] name - cocktail name
     * @param [Int] liquorId - id from a [Liquor]
     * @return [Result<Long, Error>] - id of the new [Drink.Cocktail]
     */
    suspend fun addCocktail(name: String, liquorId: Int): Result<Long, Error>
}