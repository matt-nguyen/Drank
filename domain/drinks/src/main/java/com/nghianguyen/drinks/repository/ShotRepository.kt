package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.Liquor
import kotlinx.coroutines.flow.Flow

interface ShotRepository {

    /**
     * Get a list of [Drink.Shot] for a [Liquor].
     *
     * @return [Flow<Result<List<Drink.Shot>, Error>>]
     */
    fun getShotsByLiquor(liquorId: Int): Flow<Result<List<Drink.Shot>, Error>>

    /**
     * Save a new [Drink.Shot].
     *
     * @param [String] name - shot name
     * @param [Int] liquorId - id from a [Liquor]
     * @return [Result<Long, Error>] - id of the new [Drink.Shot]
     */
    suspend fun addShot(name: String, liquorId: Int): Result<Long, Error>
}