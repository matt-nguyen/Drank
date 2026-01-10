package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.repository.WineRepository
import com.nghianguyen.drinks.usecase.request.AddBrandRequest
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddWineBrandUseCase @Inject constructor(
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(request: AddBrandRequest): Result<WineBrand, LocalDataError> {
        Log.d("AddWineBrandUseCase", request.toString())
        return wineRepository.addWineBrand(request.brandName)
    }
}