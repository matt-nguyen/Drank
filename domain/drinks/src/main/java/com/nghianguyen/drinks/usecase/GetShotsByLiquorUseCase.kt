package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.ShotRepository
import com.nghianguyen.drinks.usecase.request.GetShotsByLiquorRequest
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetShotsByLiquorUseCase @Inject constructor(
    private val shotRepository: ShotRepository
) {
    operator fun invoke(request: GetShotsByLiquorRequest): Flow<Result<List<Drink.Shot>, LocalDataError>> {
        Log.d("GetShotsByLiquorUseCase", request.toString())
        return shotRepository.getShotsByLiquor(request.liquor.id)
    }
}