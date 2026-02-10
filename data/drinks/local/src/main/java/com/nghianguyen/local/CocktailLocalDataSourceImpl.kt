package com.nghianguyen.local

import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.nghianguyen.drinks.CocktailLocalDataSource
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.local.db.daos.CocktailDao
import com.nghianguyen.local.ext.mapLocalDataError
import com.nghianguyen.local.model.cocktail.CocktailEntity
import com.nghianguyen.local.model.cocktail.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CocktailLocalDataSourceImpl @Inject constructor(
    private val cocktailDao: CocktailDao
) : CocktailLocalDataSource {
    override fun getCocktailsByLiquor(liquorId: Int): Flow<Result<List<Drink.Cocktail>, LocalDataError>> {
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return cocktailDao.getCocktailsByLiquor(liquorId).map { cocktailEntities ->
            runCatching {
                cocktailEntities.map { it.toDomain() }
            }.mapLocalDataError()
        }
    }

    override suspend fun addCocktail(
        name: String,
        liquorId: Int
    ): Result<Long, LocalDataError> {
        require(name.isNotEmpty() && name.isNotBlank()) {
            "name should not be empty nor blank: $name"
        }
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return runCatching {
            cocktailDao.addCocktail(CocktailEntity(name = name, liquorId = liquorId))
                .also { id ->
                    Log.d("CocktailLocalDataSourceImpl", "addCocktail success. id: $id")
                }
        }.mapLocalDataError()
    }
}