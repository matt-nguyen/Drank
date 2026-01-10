package com.nghianguyen.local.model.beer

import android.util.Log
import androidx.room.DatabaseView
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.drinks.model.Drink

@DatabaseView(
    "SELECT beer.id, beer.name, beer.brand_id AS brandId, beer_brand.name AS brandName, beer.style_id AS styleId, beer_style.name AS styleName " +
            "FROM beer " +
            "LEFT JOIN beer_brand ON beer.brand_id = beer_brand.id " +
            "LEFT JOIN beer_style ON beer.style_id = beer_style.id"
)
data class BeerFullView(
    val id: Int,
    val name: String,
    val brandId: Int?,
    val brandName: String?,
    val styleId: Int?,
    val styleName: String?
)

fun BeerFullView.toDomain(): Drink.Beer {
    Log.d("ASDF", "BeerFullView.toDomain(): $this")
    val brand =
        if (brandId != null && brandName != null)
            BeerBrand(brandId, brandName)
        else null

    val style =
        if (styleId != null && styleName != null)
            BeerStyle(styleId, styleName)
        else null

    return Drink.Beer(
        beerId = id,
        name = name,
        brand = brand,
        style = style
    )
}