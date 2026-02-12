package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.ValidationError
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import com.nghianguyen.drinks.repository.WineRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddWineUseCase @Inject constructor(
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(
        name: String,
        wineBrand: WineBrand,
        wineStyle: WineStyle
    ): Result<Long, Error> {
        Log.d("AddWineUseCase", "$name - $wineBrand - $wineStyle")
        if (name.isBlank()) {
            return Err(ValidationError.INVALID_INPUT)
        }
        return wineRepository.addWine(
            name, wineBrand.id, wineStyle.id
        )
    }
}