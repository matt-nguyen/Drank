package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.model.beer.BeerStyle

interface LiquorLocalDataSource {

    /**
     * Get a list of [Liquor].
     *
     * @return [Flow<Result<List<Liquor>, LocalDataError>>]
     */
    suspend fun getLiquors(): Result<List<Liquor>, LocalDataError>
}