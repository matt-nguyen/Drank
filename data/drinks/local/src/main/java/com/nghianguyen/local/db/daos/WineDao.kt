package com.nghianguyen.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nghianguyen.local.model.wine.WineBrandEntity
import com.nghianguyen.local.model.wine.WineEntity
import com.nghianguyen.local.model.wine.WineFullView
import com.nghianguyen.local.model.wine.WineStyleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WineDao {

    @Query("SELECT * FROM wine_style")
    suspend fun getWineStyles(): List<WineStyleEntity>

    @Query("SELECT * FROM wine_brand")
    fun getWineBrands(): Flow<List<WineBrandEntity>>

    @Query(
        "SELECT wine.id, wine.name, wine.brand_id AS brandId, wine_brand.name AS brandName, wine.style_id AS styleId, wine_style.name AS styleName, wine_style.wine_color AS wineColor " +
                "FROM wine " +
                "LEFT JOIN wine_brand ON wine.brand_id = wine_brand.id " +
                "LEFT JOIN wine_style ON wine.style_id = wine_style.id " +
                "WHERE brand_id = :brandId"
    )
    fun getWinesByBrand(brandId: Int): Flow<List<WineFullView>>

    @Insert
    suspend fun addWineBrand(wineBrandEntity: WineBrandEntity): Long

    @Insert
    suspend fun addWine(wineEntity: WineEntity): Long
}