package com.nghianguyen.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nghianguyen.local.model.shot.ShotEntity
import com.nghianguyen.local.model.shot.ShotFullView
import kotlinx.coroutines.flow.Flow

@Dao
interface ShotDao {

    @Query(
        "SELECT shot.id, shot.name, shot.liquor_id AS liquorId, liquor.name AS liquorName " +
                "FROM shot " +
                "LEFT JOIN liquor ON shot.liquor_id = liquor.id " +
                "WHERE liquor_id = :liquorId"
    )
    fun getShotsByLiquor(liquorId: Int): Flow<List<ShotFullView>>

    @Insert
    suspend fun addShot(shotEntity: ShotEntity): Long
}