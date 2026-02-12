package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.repository.CocktailRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetCocktailsByLiquorUseCase @Inject constructor(
    private val cocktailRepository: CocktailRepository
) {
    operator fun invoke(liquor: Liquor): Flow<Result<List<Drink.Cocktail>, Error>> {
        Log.d("GetCocktailsByLiquorUseCase", "liquor: $liquor")
        return cocktailRepository.getCocktailsByLiquor(liquor.id)
    }
}