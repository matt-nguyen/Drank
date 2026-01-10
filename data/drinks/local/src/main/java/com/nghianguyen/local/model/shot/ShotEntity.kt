package com.nghianguyen.local.model.shot

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nghianguyen.local.model.LiquorEntity

@Entity(
    tableName = "shot",
    foreignKeys = [
        ForeignKey(
            entity = LiquorEntity::class,
            parentColumns = ["id"],
            childColumns = ["liquor_id"]
        )
                  ],
    indices = [Index(name = "shot_name_unique", value = ["name"], unique = true)]
)
data class ShotEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "liquor_id", index = true)
    val liquorId: Int?,
)