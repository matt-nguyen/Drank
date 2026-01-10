package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.ConsumedDrinkRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

@ViewModelScoped
class GetConsumedDrinksByDateUseCase @Inject constructor(
    private val consumedDrinkRepository: ConsumedDrinkRepository
) {
    operator fun invoke(date: LocalDate): Flow<Result<List<ConsumedDrink>, LocalDataError>> {
        Log.d("GetConsumedDrinksByDateUseCase", date.toString())
        return consumedDrinkRepository.getConsumedDrinksByDate(date)
    }
}