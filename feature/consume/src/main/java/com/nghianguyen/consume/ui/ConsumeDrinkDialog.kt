package com.nghianguyen.consume.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.nghianguyen.consume.viewmodel.ConsumeBeerViewModel
import com.nghianguyen.consume.viewmodel.ConsumeCocktailViewModel
import com.nghianguyen.consume.viewmodel.ConsumeShotViewModel
import com.nghianguyen.consume.viewmodel.ConsumeWineViewModel

/**
 * Shows a dialog for the user to enter info on a drink they just had.
 * The dialog is determined by the ViewModel type
 */
@Composable
fun ConsumeDrinkDialog(
    viewModel: ViewModel,
    dismissDrinkDialog: () -> Unit
) {
    when (viewModel) {
        is ConsumeBeerViewModel -> { ConsumeBeerDialog(viewModel, dismissDrinkDialog) }
        is ConsumeWineViewModel -> { ConsumeWineDialog(viewModel, dismissDrinkDialog) }
        is ConsumeCocktailViewModel -> { ConsumeCocktailDialog(viewModel, dismissDrinkDialog) }
        is ConsumeShotViewModel -> { ConsumeShotDialog(viewModel, dismissDrinkDialog) }
        else -> {}
    }
}