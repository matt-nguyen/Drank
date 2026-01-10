package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.drinks.repository.BeerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BeerRepositoryImpl @Inject constructor(
    private val localDataSource: BeerLocalDataSource
): BeerRepository {

    override fun getBeersByBrand(brandId: Int): Flow<Result<List<Drink.Beer>, LocalDataError>> {
        require(brandId > 0) {
            "brandId must be > 0: $brandId"
        }
        return localDataSource.getBeersByBrand(brandId).map { beersByBrandResult ->
            beersByBrandResult.map { beers ->
                beers.sortedBy { it.name }
            }
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
        return localDataSource.addBeer(name, brandId, styleId)
    }

    override suspend fun getBeerBrands(): Flow<Result<List<BeerBrand>, LocalDataError>> {
        return localDataSource.getBeerBrands().map { beerBrandsResult ->
            beerBrandsResult.map { beerBrands ->
                beerBrands.sortedBy { it.name }
            }
        }
    }

    override suspend fun addBeerBrand(brandName: String): Result<BeerBrand, LocalDataError> {
        require(brandName.isNotEmpty() && brandName.isNotBlank()) {
            "brandName should not be empty nor blank: $brandName"
        }
        return localDataSource.addBeerBrand(brandName)
            .map { newId ->
                BeerBrand(newId.toInt(), brandName)
            }
    }

    override suspend fun getBeerStyles(): Result<List<BeerStyle>, LocalDataError> {
        return localDataSource.getBeerStyles().map { beerStyles ->
            val beerStylesFlattened = mutableListOf<BeerStyle>()
            beerStyles.forEach { beerStyle ->
                beerStylesFlattened.add(beerStyle)
                beerStyle.subBeerStyles?.forEach {
                    beerStylesFlattened.add(it)
                }
            }
            beerStylesFlattened
        }
    }
}
