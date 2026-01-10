package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.repository.BeerRepository
import com.nghianguyen.drinks.usecase.request.AddBrandRequest
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddBeerBrandUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    suspend operator fun invoke(request: AddBrandRequest): Result<BeerBrand, LocalDataError> {
        Log.d("AddBeerBrandUseCase", request.toString())
        return beerRepository.addBeerBrand(request.brandName)
    }
}