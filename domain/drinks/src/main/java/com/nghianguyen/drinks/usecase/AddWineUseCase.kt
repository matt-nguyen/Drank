package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.WineRepository
import com.nghianguyen.drinks.usecase.request.AddWineRequest
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddWineUseCase @Inject constructor(
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(request: AddWineRequest): Result<Long, LocalDataError> {
        Log.d("AddWineUseCase", request.toString())
        return wineRepository.addWine(
            request.name,
            request.wineBrand.id,
            request.wineStyle.id
        )
    }
}