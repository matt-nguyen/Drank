package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.model.LocalDataError
import kotlinx.coroutines.flow.Flow

interface ShotLocalDataSource {

    /**
     * Get a list of [Drink.Shot] for a [Liquor].
     *
     * @return [Flow<Result<List<Drink.Shot>, LocalDataError>>]
     */
    fun getShotsByLiquor(liquorId: Int): Flow<Result<List<Drink.Shot>, LocalDataError>>

    /**
     * Save a new [Drink.Shot] locally.
     *
     * @param [String] name - shot name
     * @param [Int] liquorId - id from a [Liquor]
     * @return [Result<Long, LocalDataError>] - id of the new [Drink.Shot]
     */
    suspend fun addShot(name: String, liquorId: Int): Result<Long, LocalDataError>
}