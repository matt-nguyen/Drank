package com.nghianguyen.drinks.model

import androidx.annotation.DrawableRes
import androidx.core.graphics.toColorInt
import com.nghianguyen.domain.drinks.R

enum class DrinkType(@DrawableRes val iconRes: Int, val color: Int) {
    BEER(R.drawable.sports_bar_24px, "#795548".toColorInt()),
    WINE(R.drawable.wine_bar_24px, "#9C27B0".toColorInt()),
    MIXED(R.drawable.local_bar_24px, "#009688".toColorInt()),
    SHOT(R.drawable.local_drink_24px, "#FF9800".toColorInt())
}