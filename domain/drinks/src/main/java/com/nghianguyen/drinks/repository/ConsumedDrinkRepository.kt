package com.nghianguyen.drinks.repository

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.DrinkType
import com.nghianguyen.drinks.model.Error
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.OffsetDateTime

interface ConsumedDrinkRepository {

    /**
     * Get a list of [ConsumedDrink] had on a specified date.
     *
     * @param [LocalDate] date
     * @return [Flow<Result<List<Drink.Beer>, Error>>]
     */
    fun getConsumedDrinksByDate(date: LocalDate): Flow<Result<List<ConsumedDrink>, Error>>

    /**
     * Save a new [ConsumedDrink].
     *
     * @param [String] drinkType - the type of drink. See [DrinkType]
     * @param [Int] drinkId - id from a [Drink]
     * @param [OffsetDateTime] timestamp - date and time the drink was consumed
     * @return [Result<Long, Error>] - id of the new [ConsumedDrink]
     */
    suspend fun addConsumedDrink(drinkType: String, drinkId: Int, timestamp: OffsetDateTime): Result<Long, Error>
}