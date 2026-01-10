package com.nghianguyen.home.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.nghianguyen.drinks.model.ConsumedDrink
import com.nghianguyen.theme.LocalSpacing
import java.time.format.DateTimeFormatter

@Composable
fun DrinksList(
    modifier: Modifier = Modifier,
    consumedDrinks: List<ConsumedDrink>
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(consumedDrinks.size) {
        lazyListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        val formatter = DateTimeFormatter.ofPattern("hh:mma")

        itemsIndexed(
            items = consumedDrinks,
            key = { _, consumedDrink -> consumedDrink.id }
        ) { index, consumedDrink ->
            DrinkItem(consumedDrink, formatter)

            if (index < consumedDrinks.size) {
                Spacer(modifier = Modifier.height(LocalSpacing.current.small))
            }
        }
    }
}