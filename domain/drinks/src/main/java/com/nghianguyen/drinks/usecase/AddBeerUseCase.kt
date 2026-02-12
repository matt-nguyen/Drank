package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.ValidationError
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.drinks.repository.BeerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddBeerUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    suspend operator fun invoke(
        name: String,
        beerBrand: BeerBrand,
        beerStyle: BeerStyle
    ): Result<Long, Error> {
        Log.d("AddBeerUseCase", "name: $name - beerBrand: $beerBrand - beerStyle: $beerStyle")
        if (name.isBlank()) {
            return Err(ValidationError.INVALID_INPUT)
        }
        return beerRepository.addBeer(name, beerBrand.id, beerStyle.id)
    }
}