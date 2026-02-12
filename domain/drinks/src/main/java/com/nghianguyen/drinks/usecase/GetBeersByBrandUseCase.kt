package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.repository.BeerRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetBeersByBrandUseCase @Inject constructor(
    private val beerRepository: BeerRepository
) {
    operator fun invoke(beerBrand: BeerBrand): Flow<Result<List<Drink.Beer>, Error>> {
        Log.d("GetBeersByBrandUseCase", "beerBrand: $beerBrand")
        return beerRepository.getBeersByBrand(beerBrand.id)
    }
}