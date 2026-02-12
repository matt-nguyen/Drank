package com.nghianguyen.home.viewmodel

import android.util.Log
import com.nghianguyen.base.ResourcesHelper
import com.nghianguyen.base.viewmodel.BaseViewModel
import com.nghianguyen.common.ui.R
import com.nghianguyen.drinks.usecase.GetConsumedDrinksByDateUseCase
import com.nghianguyen.home.viewmodel.home.HomeAction
import com.nghianguyen.home.viewmodel.home.HomeEvent
import com.nghianguyen.home.viewmodel.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConsumedDrinksByDateUseCase: GetConsumedDrinksByDateUseCase,
    private val drinkCooldown: DrinkCooldown,
    private val resourcesHelper: ResourcesHelper
) : BaseViewModel<HomeState, HomeAction, HomeEvent>() {
    private val selectedDate = MutableStateFlow(LocalDate.now())

    override fun buildInitialState() = HomeState(
        drinksDate = LocalDate.now(),
        drinksDateText = "",
        isToday = false,
        consumedDrinks = emptyList(),
        isLoading = true
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        launch {
            selectedDate
                .flatMapLatest { selected ->
                    getConsumedDrinksByDateUseCase(selected)
                }
                .collect { result ->
                    result.onResult(
                        onSuccess = {
                                consumedDrinks ->

                            if (consumedDrinks.size == uiState.value.consumedDrinks.size + 1) {
                                handleAction(HomeAction.StartCooldown)
                            }

                            val drinkDateText =
                                if (selectedDate.value.isToday()) resourcesHelper.getString(R.string.label_today)
                                else selectedDate.value.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))

                            updateState {
                                copy(
                                    drinksDate = selectedDate.value,
                                    drinksDateText = drinkDateText,
                                    isToday = isToday,
                                    consumedDrinks = consumedDrinks,
                                    isLoading = false
                                )
                            }
                        },
                        onFailure = {

                        }
                    )
                }
        }

        launch {
            drinkCooldown.timeLeft
                .collect { timeLeftMillis ->
                    Log.d("ASDF", "timeLeft: ${timeLeftMillis / 1000} seconds")
                }
        }
    }

    override fun handleAction(action: HomeAction) {
        Log.d("HomeViewModel", "handleAction: $action")
        launch {
            when (action) {
                HomeAction.NextDate -> {
                    selectedDate.update { it.plusDays(1) }
                }
                HomeAction.PrevDate -> {
                    selectedDate.update { it.minusDays(1) }
                }
                HomeAction.StartCooldown -> {
                    drinkCooldown.startCooldown()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

fun LocalDate.isToday(): Boolean {
    val today = LocalDate.now()
    return year == today.year
            && month == today.month
            && dayOfMonth == today.dayOfMonth
}
