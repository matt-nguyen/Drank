package com.nghianguyen.consume.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nghianguyen.common.ui.R
import com.nghianguyen.consume.viewmodel.ConsumeWineViewModel
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineAction
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineEvent
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle
import kotlinx.coroutines.flow.SharedFlow

/**
 * Dialog for the user to enter info on a glass of wine being consumed.
 */
@Composable
fun ConsumeWineDialog(
    viewModel: ConsumeWineViewModel,
    dismissDrinkDialog: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent

    ConsumeWineDialog(
        consumeWineState = uiState,
        consumeWineEvent = uiEvent,
        onWineStyleSelected = { viewModel.handleAction(ConsumeWineAction.WineStyleSelected(it)) },
        onWineBrandSelected = { viewModel.handleAction(ConsumeWineAction.WineBrandSelected(it)) },
        onWineSelected = { viewModel.handleAction(ConsumeWineAction.WineSelected(it)) },
        onSubmitNewWineBrand = { viewModel.handleAction(ConsumeWineAction.AddWineBrand(it)) },
        onSubmitNewWine = { w, wr, ws ->
            viewModel.handleAction(
                ConsumeWineAction.AddWine(
                    w,
                    wr,
                    ws
                )
            )
        },
        onSubmitConsumedWine = { viewModel.handleAction(ConsumeWineAction.SubmitConsumedWine) },
        dismissDialog = dismissDrinkDialog
    )
}

@Composable
fun ConsumeWineDialog(
    modifier: Modifier = Modifier,
    consumeWineState: ConsumeWineState,
    consumeWineEvent: SharedFlow<ConsumeWineEvent>,
    onWineStyleSelected: (WineStyle?) -> Unit,
    onWineBrandSelected: (WineBrand?) -> Unit,
    onWineSelected: (Drink.Wine?) -> Unit,
    onSubmitNewWineBrand: (String) -> Unit,
    onSubmitNewWine: (String, WineBrand, WineStyle) -> Unit,
    onSubmitConsumedWine: () -> Unit,
    dismissDialog: () -> Unit
) {
    var addBrandDialog by remember { mutableStateOf(false) }
    var addWineDialog by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }
    var addDialogErrorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        consumeWineEvent.collect {
            when (it) {
                is ConsumeWineEvent.AddWineBrandError -> {
                    addDialogErrorMsg = it.errorMsg
                }

                ConsumeWineEvent.AddWineBrandSuccess -> {
                    addBrandDialog = false
                    addDialogErrorMsg = null
                }

                is ConsumeWineEvent.AddWineError -> {
                    addDialogErrorMsg = it.errorMsg
                }

                ConsumeWineEvent.AddWineSuccess -> {
                    addWineDialog = false
                    addDialogErrorMsg = null
                }

                is ConsumeWineEvent.SubmitConsumedWineError -> {
                    errorMsg = it.errorMsg
                }

                ConsumeWineEvent.SubmitConsumedWineSuccess -> {
                    dismissDialog()
                }
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var wineStyleExpanded by remember { mutableStateOf(false) }
    var selectedWineStyleText by remember(consumeWineState.selectedStyle) {
        mutableStateOf(
            consumeWineState.selectedStyle?.name ?: defaultText
        )
    }

    var wineBrandExpanded by remember { mutableStateOf(false) }
    var selectedWineBrandText by remember(consumeWineState.selectedBrand) {
        mutableStateOf(
            consumeWineState.selectedBrand?.name ?: defaultText
        )
    }

    var wineExpanded by remember { mutableStateOf(false) }
    var selectedWineText by remember(consumeWineState.selectedWine) {
        mutableStateOf(
            consumeWineState.selectedWine?.name ?: defaultText
        )
    }

    Dialog(
        onDismissRequest = dismissDialog
    ) {
        Card(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.title_add_wine),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(32.dp))

                ExposedDropdownMenuField(
                    menuItems = consumeWineState.wineStyles,
                    text = selectedWineStyleText,
                    label = { Text(stringResource(R.string.label_style)) },
                    expanded = wineStyleExpanded,
                    onExpandedChange = { wineStyleExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineStyleExpanded = false
                        onWineStyleSelected(it)
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = consumeWineState.wineBrands,
                    text = selectedWineBrandText,
                    label = { Text(stringResource(R.string.label_brand)) },
                    expanded = wineBrandExpanded,
                    onExpandedChange = { wineBrandExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineBrandExpanded = false
                        onWineBrandSelected(it)
                    }
                )

                TextButton(
                    onClick = { addBrandDialog = true }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24px),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.button_add_new),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                ExposedDropdownMenuField(
                    menuItems = consumeWineState.wines,
                    text = selectedWineText,
                    label = { Text(stringResource(R.string.label_wine)) },
                    expanded = wineExpanded,
                    onExpandedChange = { wineExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineExpanded = false
                        onWineSelected(it)
                    }
                )

                TextButton(
                    onClick = { addWineDialog = true }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24px),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.button_add_new),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                errorMsg?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Row {
                    TextButton(onClick = dismissDialog) {
                        Text(
                            text = stringResource(R.string.button_cancel),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    TextButton(
                        onClick = onSubmitConsumedWine,
                        enabled = consumeWineState.selectedWine != null
                    ) {
                        Text(
                            text = stringResource(R.string.button_add),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

            }
        }

        if (addBrandDialog) {
            AddBrandDialog(
                submitNewBrand = onSubmitNewWineBrand,
                onDismissRequest = {
                    addBrandDialog = false
                    addDialogErrorMsg = null
                },
                errorMsg = addDialogErrorMsg
            )
        }

        if (addWineDialog) {
            AddWineDialog(
                wineStyles = consumeWineState.wineStyles,
                wineBrands = consumeWineState.wineBrands,
                submitNewWine = onSubmitNewWine,
                onDismissRequest = {
                    addWineDialog = false
                    addDialogErrorMsg = null
                },
                errorMsg = addDialogErrorMsg
            )
        }
    }
}