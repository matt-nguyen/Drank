package com.nghianguyen.local.db.daos

import androidx.room.Dao
import androidx.room.Query
import com.nghianguyen.local.model.LiquorEntity

@Dao
interface LiquorDao {

    @Query("SELECT * FROM liquor")
    suspend fun getLiquors(): List<LiquorEntity>
}