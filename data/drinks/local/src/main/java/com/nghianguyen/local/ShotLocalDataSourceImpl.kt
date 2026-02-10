package com.nghianguyen.local

import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.runCatching
import com.nghianguyen.drinks.ShotLocalDataSource
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.LocalDataError
import com.nghianguyen.local.db.daos.ShotDao
import com.nghianguyen.local.ext.mapLocalDataError
import com.nghianguyen.local.model.shot.ShotEntity
import com.nghianguyen.local.model.shot.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShotLocalDataSourceImpl @Inject constructor(
    private val shotDao: ShotDao
): ShotLocalDataSource {
    override fun getShotsByLiquor(liquorId: Int): Flow<Result<List<Drink.Shot>, LocalDataError>> {
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return shotDao.getShotsByLiquor(liquorId).map { shotEntities ->
            runCatching {
                shotEntities.map { it.toDomain() }
            }.mapLocalDataError()
        }
    }

    override suspend fun addShot(
        name: String,
        liquorId: Int
    ): Result<Long, LocalDataError> {
        require(name.isNotEmpty() && name.isNotBlank()) {
            "name should not be empty nor blank: $name"
        }
        require(liquorId > 0) {
            "liquorId must be > 0: $liquorId"
        }
        return runCatching {
            shotDao.addShot(ShotEntity(name = name, liquorId = liquorId))
                .also { id ->
                    Log.d("ShotLocalDataSourceImpl", "addShot success. id: $id")
                }
        }.mapLocalDataError()
    }
}