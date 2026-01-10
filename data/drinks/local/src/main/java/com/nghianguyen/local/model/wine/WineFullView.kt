package com.nghianguyen.local.model.wine

import androidx.room.DatabaseView
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineColor
import com.nghianguyen.drinks.model.wine.WineStyle

@DatabaseView(
    "SELECT wine.id, wine.name, wine.brand_id AS brandId, wine_brand.name AS brandName, wine.style_id AS styleId, wine_style.name AS styleName, wine_style.wine_color AS wineColor " +
            "FROM wine " +
            "LEFT JOIN wine_brand ON wine.brand_id = wine_brand.id " +
            "LEFT JOIN wine_style ON wine.style_id = wine_style.id"
)
data class WineFullView(
    val id: Int,
    val name: String,
    val brandId: Int?,
    val brandName: String?,
    val styleId: Int?,
    val styleName: String?,
    val wineColor: String?
)

fun WineFullView.toDomain(): Drink.Wine {
    val brand =
        if (brandId != null && brandName != null)
            WineBrand(brandId, brandName)
        else null

    val color = WineColor.valueOf(wineColor!!)

    val style =
        if (styleId != null && styleName != null)
            WineStyle(styleId, styleName, color)
        else null

    return Drink.Wine(
        wineId = id,
        name = name,
        brand = brand,
        style = style,
        wineColor = color
    )
}