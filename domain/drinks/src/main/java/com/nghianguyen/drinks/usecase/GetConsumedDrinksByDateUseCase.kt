package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

@ViewModelScoped
class GetConsumedDrinksByDateUseCase @Inject constructor(
    private val consumedDrinkRepository: ConsumedDrinkRepository
) {
    operator fun invoke(date: LocalDate): Flow<Result<List<ConsumedDrink>, Error>> {
        Log.d("GetConsumedDrinksByDateUseCase", date.toString())
        return consumedDrinkRepository.getConsumedDrinksByDate(date)
    }
}