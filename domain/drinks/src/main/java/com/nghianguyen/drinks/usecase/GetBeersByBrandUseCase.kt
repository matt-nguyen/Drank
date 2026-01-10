package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.repository.BeerRepository
import com.nghianguyen.drinks.usecase.request.GetBeersByBrandRequest
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetBeersByBrandUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    operator fun invoke(request: GetBeersByBrandRequest): Flow<Result<List<Drink.Beer>, LocalDataError>> {
        Log.d("GetBeersByBrandUseCase", request.toString())
        return beerRepository.getBeersByBrand(request.beerBrand.id)
    }
}