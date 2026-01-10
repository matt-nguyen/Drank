package com.nghianguyen.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nghianguyen.common.ui.R
import com.nghianguyen.drinks.model.DrinkType
import com.nghianguyen.home.ui.ConsumeDrinkButtonsBar
import com.nghianguyen.home.ui.DateSelector
import com.nghianguyen.home.ui.DrinksList
import com.nghianguyen.home.viewmodel.home.HomeState
import com.nghianguyen.theme.LocalSpacing

/**
 * The Home screen where the user can:
 * - view drinks they've consumed
 * - add drinks they've consumed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    state: HomeState,
    consumeDrinkDialog: @Composable () -> Unit,
    openConsumeDrinkDialog: (DrinkType) -> Unit,
    onPrevDate: () -> Unit = {},
    onNextDate: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) }
            )
        },
        bottomBar = {
            ConsumeDrinkButtonsBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LocalSpacing.current.large),
                onDrink = openConsumeDrinkDialog
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateSelector(
                dateText = state.drinksDateText,
                isToday = state.isToday,
                onPrevDate = onPrevDate,
                onNextDate = onNextDate
            )
            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    if (targetState.drinksDate.isAfter(initialState.drinksDate)) {
                        fadeIn() + slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) togetherWith
                                fadeOut() + slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth }
                        )
                    } else {
                        fadeIn() + slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) togetherWith
                                fadeOut() + slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth }
                        )
                    }
                },
                contentKey = { it.drinksDate }
            ) { targetState ->
                if (targetState.consumedDrinks.isNotEmpty()) {
                    DrinksList(
                        modifier = Modifier.fillMaxWidth(),
                        consumedDrinks = targetState.consumedDrinks
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (targetState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Text("No drinks on ${targetState.drinksDate}")
                        }
                    }
                }
            }
        }

        consumeDrinkDialog()
    }
}
