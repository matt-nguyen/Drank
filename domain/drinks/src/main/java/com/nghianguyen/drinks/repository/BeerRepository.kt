package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import kotlinx.coroutines.flow.Flow

interface BeerRepository {

    /**
     * Get a list of [Drink.Beer] for a beer brand.
     *
     * @param [Int] brandId - id from a [BeerBrand]
     * @return [Flow<Result<List<Drink.Beer>, Error>>]
     */
    fun getBeersByBrand(brandId: Int): Flow<Result<List<Drink.Beer>, Error>>

    /**
     * Save a new [Drink.Beer].
     *
     * @param [String] name - beer name
     * @param [Int] brandId - id from a [BeerBrand]
     * @param [Int] styleId - id from a [BeerStyle]
     * @return [Result<Long, Error>] - id of the new [Drink.Beer]
     */
    suspend fun addBeer(name: String, brandId: Int, styleId: Int): Result<Long, Error>

    /**
     * Get a list of [BeerBrand].
     *
     * @return [Flow<Result<List<BeerBrand>, Error>>]
     */
    suspend fun getBeerBrands(): Flow<Result<List<BeerBrand>, Error>>

    /**
     * Save a new [BeerBrand].
     *
     * @param [String] brandName - name of the beer brand
     * @return [Result<Long, Error>] - id of the new [BeerBrand]
     */
    suspend fun addBeerBrand(brandName: String): Result<BeerBrand, Error>

    /**
     * Get a list of [BeerStyle].
     *
     * @return [Flow<Result<List<BeerStyle>, Error>>]
     */
    suspend fun getBeerStyles(): Result<List<BeerStyle>, Error>
}