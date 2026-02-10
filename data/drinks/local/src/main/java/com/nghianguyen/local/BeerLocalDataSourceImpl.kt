package com.nghianguyen.local

import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.nghianguyen.drinks.BeerLocalDataSource
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.local.db.daos.BeerDao
import com.nghianguyen.local.ext.mapLocalDataError
import com.nghianguyen.local.model.beer.BeerBrandEntity
import com.nghianguyen.local.model.beer.BeerEntity
import com.nghianguyen.local.model.beer.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BeerLocalDataSourceImpl @Inject constructor(
    private val beerDao: BeerDao
): BeerLocalDataSource {

    override fun getBeersByBrand(brandId: Int): Flow<Result<List<Drink.Beer>, LocalDataError>> {
        require(brandId > 0) {
            "brandId must be > 0: $brandId"
        }
        return beerDao.getBeersByBrand(brandId).map { beerEntities ->
            runCatching {
                beerEntities.map { it.toDomain() }
            }.mapLocalDataError()
        }
    }

    override suspend fun addBeer(
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
            beerDao.addBeer(BeerEntity(name = name, brandId = brandId, styleId = styleId))
                .also { id ->
                    Log.d("BeerLocalDataSourceImpl", "addBeer success. id: $id")
                }
        }.mapLocalDataError()
    }

    override fun getBeerBrands(): Flow<Result<List<BeerBrand>, LocalDataError>> {
        return beerDao.getBeerBrands().map { beerBrandEntities ->
            runCatching {
                beerBrandEntities.map { it.toDomain() }
            }.mapLocalDataError()
        }
    }

    override suspend fun addBeerBrand(brandName: String): Result<Long, LocalDataError> {
        require(brandName.isNotEmpty() && brandName.isNotBlank()) {
            "brandName should not be empty nor blank: $brandName"
        }
        return runCatching {
            beerDao.addBeerBrand(BeerBrandEntity(name = brandName))
                .also { id ->
                    Log.d("BeerLocalDataSourceImpl", "addBeerBrand success. id: $id")
                }
        }.mapLocalDataError()
    }

    override suspend fun getBeerStyles(): Result<List<BeerStyle>, LocalDataError> {
        return runCatching {
            beerDao.getFullBeerStyles().map { it.toDomain() }
        }.mapLocalDataError()
    }
}
