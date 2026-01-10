package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Liquor

interface LiquorRepository {

    /**
     * Get a list of [Liquor].
     *
     * @return [Flow<Result<List<Liquor>, LocalDataError>>]
     */
    suspend fun getLiquors(): Result<List<Liquor>, LocalDataError>
}