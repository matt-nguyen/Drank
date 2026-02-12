package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.ValidationError
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.repository.BeerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddBeerBrandUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    suspend operator fun invoke(brandName: String): Result<BeerBrand, Error> {
        Log.d("AddBeerBrandUseCase", "brandName: $brandName")
        if (brandName.isBlank()) {
            return Err(ValidationError.INVALID_INPUT)
        }
        return beerRepository.addBeerBrand(brandName)
    }
}