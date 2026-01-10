package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.repository.WineRepository
import com.nghianguyen.drinks.usecase.request.GetWinesByBrandRequest
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetWinesByBrandUseCase @Inject constructor(
    private val wineRepository: WineRepository
) {
    operator fun invoke(request: GetWinesByBrandRequest): Flow<Result<List<Drink.Wine>, LocalDataError>> {
        Log.d("GetWinesByBrandUseCase", request.toString())
        return wineRepository.getWinesByBrand(request.wineBrand.id)
    }
}