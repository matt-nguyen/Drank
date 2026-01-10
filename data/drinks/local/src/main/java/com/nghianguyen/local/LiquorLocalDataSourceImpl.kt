package com.nghianguyen.local

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.nghianguyen.drinks.LiquorLocalDataSource
import com.nghianguyen.drinks.model.Liquor
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.drinks.model.ext.mapLocalDataError
import com.nghianguyen.local.db.daos.LiquorDao
import com.nghianguyen.local.model.toDomain
import javax.inject.Inject

class LiquorLocalDataSourceImpl @Inject constructor(
    private val liquorDao: LiquorDao
): LiquorLocalDataSource {
    override suspend fun getLiquors(): Result<List<Liquor>, LocalDataError> {
        return runCatching {
            liquorDao.getLiquors().map { it.toDomain() }
        }.mapLocalDataError()
    }
}
