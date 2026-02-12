package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.Liquor

interface LiquorRepository {

    /**
     * Get a list of [Liquor].
     *
     * @return [Flow<Result<List<Liquor>, Error>>]
     */
    suspend fun getLiquors(): Result<List<Liquor>, Error>
}