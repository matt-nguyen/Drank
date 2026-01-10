package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import com.nghianguyen.drinks.usecase.request.AddConsumedDrinkRequest
import java.time.OffsetDateTime
import javax.inject.Inject

class AddConsumedDrinkUseCase @Inject constructor(
    private val consumedDrinkRepository: ConsumedDrinkRepository
) {
    suspend operator fun invoke(request: AddConsumedDrinkRequest): Result<Long, LocalDataError> {
        Log.d("AddConsumedDrinkUseCase", request.toString())
        val drinkType = when (request.drink) {
            is Drink.Beer -> "beer"
            is Drink.Cocktail -> "cocktail"
            is Drink.Shot -> "shot"
            is Drink.Wine -> "wine"
        }

        return consumedDrinkRepository.addConsumedDrink(
            drinkType, request.drink.id,
            OffsetDateTime.now()
        )
    }
}