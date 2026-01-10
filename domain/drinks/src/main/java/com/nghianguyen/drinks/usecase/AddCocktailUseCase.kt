package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.CocktailRepository
import com.nghianguyen.drinks.usecase.request.AddCocktailRequest
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddCocktailUseCase @Inject constructor(
    private val cocktailRepository: CocktailRepository
) {
    suspend operator fun invoke(request: AddCocktailRequest): Result<Long, LocalDataError> {
        Log.d("AddCocktailUseCase", request.toString())
        return cocktailRepository.addCocktail(request.name, request.liquor.id)
    }
}