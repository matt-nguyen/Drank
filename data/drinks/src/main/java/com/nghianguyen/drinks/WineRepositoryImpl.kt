package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import com.nghianguyen.drinks.repository.WineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WineRepositoryImpl @Inject constructor(
    private val localDataSource: WineLocalDataSource
): WineRepository {
    override suspend fun getWineStyles(): Result<List<WineStyle>, Error> {
        return localDataSource.getWineStyles().map { wineStyles ->
            wineStyles.sortedBy { it.name }
        }
    }

    override fun getWineBrands(): Flow<Result<List<WineBrand>, Error>> {
        return localDataSource.getWineBrands().map { wineBrandsResult ->
            wineBrandsResult.map { wineBrands ->
                wineBrands.sortedBy { it.name }
            }
        }
    }

    override fun getWinesByBrand(brandId: Int): Flow<Result<List<Drink.Wine>, Error>> {
        require(brandId > 0) {
            "brandId must be > 0: $brandId"
        }
        return localDataSource.getWinesByBrand(brandId).map { winesByBrandResult ->
            winesByBrandResult.map { wines ->
                wines.sortedBy { it.name }
            }
        }
    }

    override suspend fun addWineBrand(brandName: String): Result<WineBrand, Error> {
        require(brandName.isNotEmpty() && brandName.isNotBlank()) {
            "brandName should not be empty nor blank: $brandName"
        }
        return localDataSource.addWineBrand(brandName)
            .map { newId -> WineBrand(newId.toInt(), brandName) }
    }

    override suspend fun addWine(
        name: String,
        brandId: Int,
        styleId: Int
    ): Result<Long, Error> {
        require(name.isNotEmpty() && name.isNotBlank()) {
            "name should not be empty nor blank: $name"
        }
        require(brandId > 0) {
            "brandId must be > 0: $brandId"
        }
        require(styleId > 0) {
            "styleId must be > 0: $styleId"
        }
        return localDataSource.addWine(name, brandId, styleId)
    }
}
