package com.nghianguyen.local

import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.nghianguyen.drinks.WineLocalDataSource
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.ext.mapLocalDataError
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import com.nghianguyen.local.db.daos.WineDao
import com.nghianguyen.local.model.wine.WineBrandEntity
import com.nghianguyen.local.model.wine.WineEntity
import com.nghianguyen.local.model.wine.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WineLocalDataSourceImpl @Inject constructor(
    private val wineDao: WineDao
): WineLocalDataSource {
    override suspend fun getWineStyles(): Result<List<WineStyle>, LocalDataError> {
        return runCatching {
            wineDao.getWineStyles().map { it.toDomain() }
        }.mapLocalDataError()
    }

    override fun getWineBrands(): Flow<Result<List<WineBrand>, LocalDataError>> {
        return wineDao.getWineBrands().map { wineBrandEntities ->
            runCatching {
                wineBrandEntities.map { it.toDomain() }
            }.mapLocalDataError()
        }
    }

    override fun getWinesByBrand(brandId: Int): Flow<Result<List<Drink.Wine>, LocalDataError>> {
        require(brandId > 0) {
            "brandId must be > 0: $brandId"
        }
        return wineDao.getWinesByBrand(brandId).map { wineEntities ->
            runCatching {
                wineEntities.map { it.toDomain() }
            }.mapLocalDataError()
        }
    }

    override suspend fun addWineBrand(brandName: String): Result<Long, LocalDataError> {
        require(brandName.isNotEmpty() && brandName.isNotBlank()) {
            "brandName should not be empty nor blank: $brandName"
        }
        return runCatching {
            wineDao.addWineBrand(WineBrandEntity(name = brandName))
                .also { id ->
                    Log.d("WineLocalDataSourceImpl", "addWineBrand success. id: $id")
                }
        }.mapLocalDataError()
    }

    override suspend fun addWine(
        name: String,
        brandId: Int,
        styleId: Int
    ): Result<Long, LocalDataError> {
        require(name.isNotEmpty() && name.isNotBlank()) {
            "name should not be empty nor blank: $name"
        }
        require(brandId > 0) {
            "brandId must be > 0: $brandId"
        }
        require(styleId > 0) {
            "styleId must be > 0: $styleId"
        }
        return runCatching {
            wineDao.addWine(WineEntity(name = name, brandId = brandId, styleId = styleId))
                .also { id ->
                    Log.d("WineLocalDataSourceImpl", "addWine success. id: $id")
                }
        }.mapLocalDataError()
    }
}