package com.nghianguyen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nghianguyen.consume.consumeDrinkViewModel
import com.nghianguyen.consume.ui.ConsumeDrinkDialog
import com.nghianguyen.drinks.model.DrinkType
import com.nghianguyen.home.viewmodel.HomeViewModel
import com.nghianguyen.home.viewmodel.home.HomeAction
import com.nghianguyen.home.viewmodel.home.HomeState
import kotlinx.serialization.Serializable

@Serializable
data object HomeDestination

fun NavGraphBuilder.homeScreen() {
    composable<HomeDestination> {
        val viewModel: HomeViewModel = hiltViewModel()
        val homeUiState: HomeState by viewModel.uiState.collectAsStateWithLifecycle()

        var drinkType by remember { mutableStateOf<DrinkType?>(null) }
        val consumeViewModel: ViewModel? = consumeDrinkViewModel(drinkType)

        HomeScreen(
            state = homeUiState,
            consumeDrinkDialog = @Composable {
                if (consumeViewModel != null) {
                    ConsumeDrinkDialog(consumeViewModel) { drinkType = null }
                }
            },
            onPrevDate = { viewModel.handleAction(HomeAction.PrevDate) },
            onNextDate = { viewModel.handleAction(HomeAction.NextDate) },
            openConsumeDrinkDialog = { drinkType = it }
        )
    }
}
