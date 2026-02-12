package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.repository.ShotRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetShotsByLiquorUseCase @Inject constructor(
    private val shotRepository: ShotRepository
) {
    operator fun invoke(liquor: Liquor): Flow<Result<List<Drink.Shot>, Error>> {
        Log.d("GetShotsByLiquorUseCase", "liquor: $liquor")
        return shotRepository.getShotsByLiquor(liquor.id)
    }
}