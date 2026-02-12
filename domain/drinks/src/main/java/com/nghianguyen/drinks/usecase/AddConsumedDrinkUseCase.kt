package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import java.time.OffsetDateTime
import javax.inject.Inject

class AddConsumedDrinkUseCase @Inject constructor(
    private val consumedDrinkRepository: ConsumedDrinkRepository
) {
    suspend operator fun invoke(drink: Drink): Result<Long, Error> {
        Log.d("AddConsumedDrinkUseCase", "drink: $drink")
        val drinkType = when (drink) {
            is Drink.Beer -> "beer"
            is Drink.Cocktail -> "cocktail"
            is Drink.Shot -> "shot"
            is Drink.Wine -> "wine"
        }
        return consumedDrinkRepository.addConsumedDrink(
            drinkType, drink.id,
            OffsetDateTime.now()
        )
    }
}