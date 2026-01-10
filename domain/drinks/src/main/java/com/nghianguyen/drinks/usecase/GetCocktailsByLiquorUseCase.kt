package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.repository.CocktailRepository
import com.nghianguyen.drinks.usecase.request.GetCocktailsByLiquorRequest
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetCocktailsByLiquorUseCase @Inject constructor(
    private val cocktailRepository: CocktailRepository
) {
    operator fun invoke(request: GetCocktailsByLiquorRequest): Flow<Result<List<Drink.Cocktail>, LocalDataError>> {
        Log.d("GetCocktailsByLiquorUseCase", request.toString())
        return cocktailRepository.getCocktailsByLiquor(request.liquor.id)
    }
}