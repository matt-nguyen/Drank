package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject

class ConsumedDrinkRepositoryImpl @Inject constructor(
    private val localDataSource: ConsumedDrinksLocalDataSource
): ConsumedDrinkRepository {

    override fun getConsumedDrinksByDate(date: LocalDate): Flow<Result<List<ConsumedDrink>, Error>> {
        return localDataSource.getConsumedDrinksByDate(date)
    }

    override suspend fun addConsumedDrink(
        drinkType: String,
        drinkId: Int,
        timestamp: OffsetDateTime
    ): Result<Long, Error> {
        return localDataSource.addConsumedDrink(drinkType, drinkId, timestamp)
    }
}
