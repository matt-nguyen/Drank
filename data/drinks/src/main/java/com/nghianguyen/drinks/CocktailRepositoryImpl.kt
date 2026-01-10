package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val localDataSource: CocktailLocalDataSource
): CocktailRepository {
    override fun getCocktailsByLiquor(liquorId: Int): Flow<Result<List<Drink.Cocktail>, LocalDataError>> {
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return localDataSource.getCocktailsByLiquor(liquorId)
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
        return localDataSource.addCocktail(name, liquorId)
    }
}
