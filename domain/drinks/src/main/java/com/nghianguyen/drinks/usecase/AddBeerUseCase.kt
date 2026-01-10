package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.BeerRepository
import com.nghianguyen.drinks.usecase.request.AddBeerRequest
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddBeerUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    suspend operator fun invoke(request: AddBeerRequest): Result<Long, LocalDataError> {
        Log.d("AddBeerUseCase", request.toString())
        return beerRepository.addBeer(
            request.name,
            request.beerBrand.id,
            request.beerStyle.id
        )
    }
}