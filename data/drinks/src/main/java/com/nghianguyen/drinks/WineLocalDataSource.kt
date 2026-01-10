package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import kotlinx.coroutines.flow.Flow

interface WineLocalDataSource {

    /**
     * Get a list of [WineStyle].
     *
     * @return [Flow<Result<List<WineStyle>, LocalDataError>>]
     */
    suspend fun getWineStyles(): Result<List<WineStyle>, LocalDataError>

    /**
     * Get a list of [WineBrand].
     *
     * @return [Flow<Result<List<WineBrand>, LocalDataError>>]
     */
    fun getWineBrands(): Flow<Result<List<WineBrand>, LocalDataError>>

    /**
     * Get a list of [Drink.Wine] for a wine brand.
     *
     * @param [Int] id - id from a [WineBrand]
     * @return [Flow<Result<List<Drin.Wine>, LocalDataError>>]
     */
    fun getWinesByBrand(brandId: Int): Flow<Result<List<Drink.Wine>, LocalDataError>>

    /**
     * Save a new [WineBrand] locally.
     *
     * @param [String] brandName - wine brand name
     * @return [Result<Long, LocalDataError>] - id of the new [WineBrand]
     */
    suspend fun addWineBrand(brandName: String): Result<Long, LocalDataError>

    /**
     * Save a new [Drink.Wine] locally.
     *
     * @param [String] name - wine name
     * @param [Int] brandId - id from a [WineBrand]
     * @param [Int] styleId - id from a [WineStyle]
     * @return [Result<Long, LocalDataError>] - id of the new [Drink.Wine]
     */
    suspend fun addWine(name: String, brandId: Int, styleId: Int): Result<Long, LocalDataError>

}