package com.nghianguyen.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.nghianguyen.common.ui.R
import com.nghianguyen.drinks.model.DrinkType

@Composable
fun ConsumeDrinkButtonsBar(
    modifier: Modifier = Modifier,
    onDrink: (DrinkType) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DrinkType.entries.forEach { drinkType ->
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(drinkType.color)
                ),
                onClick = { onDrink(drinkType) }
            ) {
                Icon(
                    painter = painterResource(drinkType.iconRes),
                    contentDescription = null
                )
                Icon(
                    painter = painterResource(R.drawable.add_24px),
                    contentDescription = null
                )
            }
        }
    }
}
