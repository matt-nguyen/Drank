package com.nghianguyen.home.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nghianguyen.common.ui.R
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.DrinkType
import com.nghianguyen.theme.LocalSpacing
import java.time.format.DateTimeFormatter

@Composable
fun DrinkItem(
    consumedDrink: ConsumedDrink,
    formatter: DateTimeFormatter?
) {
    val drink = consumedDrink.drink
    val drinkType = when (drink) {
        is Drink.Beer -> "BEER"
        is Drink.Cocktail -> "COCKTAIL"
        is Drink.Shot -> "SHOT"
        is Drink.Wine -> "WINE"
    }

    val drinkMainText = when (drink) {
        is Drink.Beer -> "${drink.brand?.name} - ${drink.name}"
        is Drink.Cocktail -> drink.name
        is Drink.Shot -> drink.name
        is Drink.Wine -> "${drink.brand?.name} - ${drink.name}"
    }


    val drinkTagText = when (drink) {
        is Drink.Beer -> drink.style?.name ?: "None"
        is Drink.Cocktail -> drink.liquor.name
        is Drink.Shot -> drink.liquor.name
        is Drink.Wine -> drink.style?.name ?: "None"
    }


    val iconRes = when (drink) {
        is Drink.Beer -> DrinkType.BEER.iconRes
        is Drink.Cocktail -> DrinkType.MIXED.iconRes
        is Drink.Shot -> DrinkType.SHOT.iconRes
        is Drink.Wine -> DrinkType.WINE.iconRes
    }


    val drinkTypeTint = Color(
        when (drink) {
            is Drink.Beer -> DrinkType.BEER.color
            is Drink.Cocktail -> DrinkType.MIXED.color
            is Drink.Shot -> DrinkType.SHOT.color
            is Drink.Wine -> DrinkType.WINE.color
        }
    )

    val iconDp = 80.dp

    val padding = LocalSpacing.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding.medium)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = drinkType,
                modifier = Modifier.padding(start = iconDp).background(drinkTypeTint.copy(alpha = 0.25f)),
                color = drinkTypeTint,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                start = iconDp,
                                bottom = padding.xSmall,
                                top = padding.xSmall,
                                end = padding.small
                            )
                    ) {
                        Text(
                            text = drinkMainText,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = drinkTagText,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }


        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            modifier = Modifier
                .size(iconDp)
                .align(Alignment.BottomStart),
            contentDescription = null,
            tint = drinkTypeTint
        )

        Row(
            modifier = Modifier
                .padding(end = padding.small, bottom = padding.xSmall)
                .align(Alignment.BottomEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val onPrimaryContainerTransparent = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.nest_clock_farsight_analog_24px),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = onPrimaryContainerTransparent
            )
            Spacer(modifier = Modifier.width(padding.xSmall))
            Text(
                text = consumedDrink.timestamp.format(formatter),
                style = MaterialTheme.typography.labelSmall,
                color = onPrimaryContainerTransparent
            )
        }
    }
}
