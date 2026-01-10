package com.nghianguyen.consume

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.nghianguyen.consume.viewmodel.ConsumeBeerViewModel
import com.nghianguyen.consume.viewmodel.ConsumeCocktailViewModel
import com.nghianguyen.consume.viewmodel.ConsumeShotViewModel
import com.nghianguyen.consume.viewmodel.ConsumeWineViewModel
import com.nghianguyen.drinks.model.DrinkType

/**
 * Gets a [com.nghianguyen.consume.viewmodel.ConsumeDrinkViewModel] by drink type.
 *
 * @param [DrinkType] drinkType - the type of drink
 * @return [ViewModel?]
 */
@Composable
fun consumeDrinkViewModel(drinkType: DrinkType?): ViewModel? {
    val addBeerViewModel: ConsumeBeerViewModel = hiltViewModel()
    val addWineViewModel: ConsumeWineViewModel = hiltViewModel()
    val addCocktailViewModel: ConsumeCocktailViewModel = hiltViewModel()
    val addShotViewModel: ConsumeShotViewModel = hiltViewModel()

    return when (drinkType) {
        DrinkType.BEER -> addBeerViewModel
        DrinkType.MIXED -> addCocktailViewModel
        DrinkType.SHOT -> addShotViewModel
        DrinkType.WINE -> addWineViewModel
        null -> null
    }
}
