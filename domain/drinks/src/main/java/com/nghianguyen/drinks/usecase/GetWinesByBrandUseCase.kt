package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Error
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.repository.WineRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetWinesByBrandUseCase @Inject constructor(
    private val wineRepository: WineRepository
) {
    operator fun invoke(wineBrand: WineBrand): Flow<Result<List<Drink.Wine>, Error>> {
        Log.d("GetWinesByBrandUseCase", "wineBrand: $wineBrand")
        return wineRepository.getWinesByBrand(wineBrand.id)
    }
}