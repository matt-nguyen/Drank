package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject

class ConsumedDrinkRepositoryImpl @Inject constructor(
    private val localDataSource: ConsumedDrinksLocalDataSource
): ConsumedDrinkRepository {

    override fun getConsumedDrinksByDate(date: LocalDate): Flow<Result<List<ConsumedDrink>, LocalDataError>> {
        return localDataSource.getConsumedDrinksByDate(date)
    }

    override suspend fun addConsumedDrink(
        drinkType: String,
        drinkId: Int,
        timestamp: OffsetDateTime
    ): Result<Long, LocalDataError> {
        return localDataSource.addConsumedDrink(drinkType, drinkId, timestamp)
    }
}
