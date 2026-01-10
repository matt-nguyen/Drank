package com.nghianguyen.drinks.usecase

import android.util.Log
import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.repository.ShotRepository
import com.nghianguyen.drinks.usecase.request.AddShotRequest
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddShotUseCase @Inject constructor(
    private val shotRepository: ShotRepository
) {
    suspend operator fun invoke(request: AddShotRequest): Result<Long, LocalDataError> {
        Log.d("AddShotUseCase", request.toString())
        return shotRepository.addShot(request.name, request.liquor.id)
    }
}