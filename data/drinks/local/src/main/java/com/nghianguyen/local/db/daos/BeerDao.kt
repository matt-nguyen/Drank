package com.nghianguyen.local.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nghianguyen.local.model.beer.BeerBrandEntity
import com.nghianguyen.local.model.beer.BeerEntity
import com.nghianguyen.local.model.beer.BeerFullView
import com.nghianguyen.local.model.beer.BeerStyleWithSubs
import kotlinx.coroutines.flow.Flow

@Dao
interface BeerDao {
    @Query("SELECT * FROM beer_style WHERE parent_style_id IS NULL")
    suspend fun getFullBeerStyles(): List<BeerStyleWithSubs>

    @Query("SELECT * FROM beer_brand")
    fun getBeerBrands(): Flow<List<BeerBrandEntity>>

    @Query(
        "SELECT beer.id, beer.name, beer.brand_id AS brandId, beer_brand.name AS brandName, beer.style_id AS styleId, beer_style.name AS styleName " +
                "FROM beer " +
                "LEFT JOIN beer_brand ON beer.brand_id = beer_brand.id " +
                "LEFT JOIN beer_style ON beer.style_id = beer_style.id " +
                "WHERE brand_id = :brandId "
    )
    fun getBeersByBrand(brandId: Int): Flow<List<BeerFullView>>

    @Insert
    suspend fun addBeer(beerEntity: BeerEntity): Long

    @Insert
    suspend fun addBeerBrand(beerBrandEntity: BeerBrandEntity): Long
}