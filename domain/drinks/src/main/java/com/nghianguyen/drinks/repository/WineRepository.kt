package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import kotlinx.coroutines.flow.Flow

interface WineRepository {

    /**
     * Get a list of [WineStyle].
     *
     * @return [Flow<Result<List<WineStyle>, Error>>]
     */
    suspend fun getWineStyles(): Result<List<WineStyle>, Error>

    /**
     * Get a list of [WineBrand].
     *
     * @return [Flow<Result<List<WineBrand>, Error>>]
     */
    fun getWineBrands(): Flow<Result<List<WineBrand>, Error>>

    /**
     * Get a list of [Drink.Wine] for a wine brand.
     *
     * @param [Int] id - id from a [WineBrand]
     * @return [Flow<Result<List<Drin.Wine>, Error>>]
     */
    fun getWinesByBrand(brandId: Int): Flow<Result<List<Drink.Wine>, Error>>

    /**
     * Save a new [WineBrand].
     *
     * @param [String] brandName - wine brand name
     * @return [Result<Long, Error>] - id of the new [WineBrand]
     */
    suspend fun addWineBrand(brandName: String): Result<WineBrand, Error>

    /**
     * Save a new [Drink.Wine].
     *
     * @param [String] name - wine name
     * @param [Int] brandId - id from a [WineBrand]
     * @param [Int] styleId - id from a [WineStyle]
     * @return [Result<Long, Error>] - id of the new [Drink.Wine]
     */
    suspend fun addWine(name: String, brandId: Int, styleId: Int): Result<Long, Error>
}