package com.nghianguyen.drinks

import com.github.michaelbull.result.Result
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.repository.LiquorRepository
import javax.inject.Inject

class LiquorRepositoryImpl @Inject constructor(
    private val localDataSource: LiquorLocalDataSource
): LiquorRepository {
    override suspend fun getLiquors(): Result<List<Liquor>, LocalDataError> {
        return localDataSource.getLiquors()
    }
}
