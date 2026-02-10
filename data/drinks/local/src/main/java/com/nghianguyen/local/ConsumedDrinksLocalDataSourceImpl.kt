package com.nghianguyen.local

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.nghianguyen.drinks.ConsumedDrinksLocalDataSource
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.local.db.daos.ConsumedDrinkDao
import com.nghianguyen.local.ext.mapLocalDataError
import com.nghianguyen.local.model.ConsumedDrinkEntity
import com.nghianguyen.local.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ConsumedDrinksLocalDataSourceImpl @Inject constructor(
    private val consumedDrinkDao: ConsumedDrinkDao
): ConsumedDrinksLocalDataSource {

    override fun getConsumedDrinksByDate(date: LocalDate): Flow<Result<List<ConsumedDrink>, LocalDataError>> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val start = date
            .atTime(5, 0).format(formatter)
        val end = date.plusDays(1)
            .atTime(5, 0).format(formatter)

        Log.d("getConsumedDrinksByDate", "Getting consumed drinks from $start to $end")

        return combine(
            consumedDrinkDao.getConsumedBeers(start, end).map { it.map { it.toDomain() } },
            consumedDrinkDao.getConsumedWines(start, end).map { it.map { it.toDomain() } },
            consumedDrinkDao.getConsumedShots(start, end).map { it.map { it.toDomain() } },
            consumedDrinkDao.getConsumedCocktails(start, end).map { it.map { it.toDomain() } }
        ) { consumedBeers, consumedWines, consumedShots, consumedCocktails ->
            Ok((consumedBeers  + consumedWines + consumedShots + consumedCocktails).sortedByDescending { it.timestamp })
        }.catch {
            Log.e("getConsumedDrinksByDate", "Error getting consumed drinks", it)
            Err(LocalDataError.UNKNOWN)
        }
    }

    override suspend fun addConsumedDrink(
        drinkType: String,
        drinkId: Int,
        timestamp: OffsetDateTime
    ): Result<Long, LocalDataError> {
        return runCatching {
            val entity = ConsumedDrinkEntity(
                drinkType = drinkType,
                drinkId = drinkId,
                timestamp = timestamp
            )
            consumedDrinkDao.addConsumedDrink(entity)
                .also { id ->
                    Log.d("ConsumedDrinksLocalDataSourceImpl", "addConsumedDrink success. id: $id")
                }
        }.mapLocalDataError()
    }
}