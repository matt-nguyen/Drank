package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.model.ValidationError
import com.nghianguyen.drinks.repository.CocktailRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddCocktailUseCase @Inject constructor(
    private val cocktailRepository: CocktailRepository
) {
    suspend operator fun invoke(name: String, liquor: Liquor): Result<Long, Error> {
        Log.d("AddCocktailUseCase", "name: $name - liquor: $liquor")
        if (name.isBlank()) {
            return Err(ValidationError.INVALID_INPUT)
        }
        return cocktailRepository.addCocktail(name, liquor.id)
    }
}