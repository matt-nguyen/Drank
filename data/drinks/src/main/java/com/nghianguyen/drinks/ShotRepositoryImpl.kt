package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.repository.ShotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShotRepositoryImpl @Inject constructor(
    private val localDataSource: ShotLocalDataSource
): ShotRepository {
    override fun getShotsByLiquor(liquorId: Int): Flow<Result<List<Drink.Shot>, Error>> {
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return localDataSource.getShotsByLiquor(liquorId)
    }

    override suspend fun addShot(
        name: String,
        liquorId: Int
    ): Result<Long, Error> {
        require(name.isNotEmpty() && name.isNotBlank()) {
            "name should not be empty nor blank: $name"
        }
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return localDataSource.addShot(name, liquorId)
    }
}
