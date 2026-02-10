package com.nghianguyen.consume.ui.wine

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nghianguyen.CollectFlowEffect
import com.nghianguyen.common.ui.R
import com.nghianguyen.consume.ui.AddBrandDialog
import com.nghianguyen.consume.ui.ExposedDropdownMenuField

import com.nghianguyen.consume.viewmodel.ConsumeWineViewModel
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineAction
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineDialogState
import com.nghianguyen.consume.viewmodel.wine.ConsumeWineEvent
import com.nghianguyen.text.toStringText
import com.nghianguyen.theme.LocalSpacing
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
        state = uiState,
        event = uiEvent,
        onAction = { viewModel.handleAction(it) },
        dismissDialog = dismissDrinkDialog
    )
}

@Composable
fun ConsumeWineDialog(
    modifier: Modifier = Modifier,
    state: ConsumeWineDialogState,
    event: SharedFlow<ConsumeWineEvent>,
    onAction: (ConsumeWineAction) -> Unit,
    dismissDialog: () -> Unit
) {
    CollectFlowEffect(event) {
        when (it) {
            ConsumeWineEvent.SubmitConsumedWineSuccess -> {
                dismissDialog()
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var wineStyleExpanded by remember { mutableStateOf(false) }
    var selectedWineStyleText by remember(state.selectedStyle) {
        mutableStateOf(
            state.selectedStyle?.name ?: defaultText
        )
    }

    var wineBrandExpanded by remember { mutableStateOf(false) }
    var selectedWineBrandText by remember(state.selectedBrand) {
        mutableStateOf(
            state.selectedBrand?.name ?: defaultText
        )
    }

    var wineExpanded by remember { mutableStateOf(false) }
    var selectedWineText by remember(state.selectedWine) {
        mutableStateOf(
            state.selectedWine?.name ?: defaultText
        )
    }

    val context = LocalContext.current
    var errorMsg by remember(state.errorMsg) {
        mutableStateOf(state.errorMsg?.toStringText(context))
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
                    .padding(LocalSpacing.current.medium)
            ) {
                Text(
                    text = stringResource(R.string.title_add_wine),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(LocalSpacing.current.large))

                ExposedDropdownMenuField(
                    menuItems = state.wineStyles,
                    text = selectedWineStyleText,
                    label = { Text(stringResource(R.string.label_style)) },
                    expanded = wineStyleExpanded,
                    onExpandedChange = { wineStyleExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineStyleExpanded = false
                        onAction(ConsumeWineAction.StyleSelected(it))
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = state.wineBrands,
                    text = selectedWineBrandText,
                    label = { Text(stringResource(R.string.label_brand)) },
                    expanded = wineBrandExpanded,
                    onExpandedChange = { wineBrandExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineBrandExpanded = false
                        onAction(ConsumeWineAction.BrandSelected(it))
                    }
                )

                TextButton(
                    onClick = { onAction(ConsumeWineAction.OpenAddBrandDialog) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24px),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(LocalSpacing.current.small))
                    Text(
                        text = stringResource(R.string.button_add_new),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                ExposedDropdownMenuField(
                    menuItems = state.wines,
                    text = selectedWineText,
                    label = { Text(stringResource(R.string.label_wine)) },
                    expanded = wineExpanded,
                    onExpandedChange = { wineExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineExpanded = false
                        onAction(ConsumeWineAction.WineSelected(it))
                    }
                )

                TextButton(
                    onClick = { onAction(ConsumeWineAction.OpenAddWineDialog) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add_24px),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(LocalSpacing.current.small))
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
                    Spacer(modifier = Modifier.width(LocalSpacing.current.medium))

                    TextButton(
                        onClick = { onAction(ConsumeWineAction.SubmitConsumedWine) },
                        enabled = state.selectedWine != null
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

        val addDialog = state.addDialogState
        when (addDialog) {
            is WineAddDialogType.AddBrand -> {
                AddBrandDialog(
                    state = addDialog.addDialogState,
                    submitNewBrand = { onAction(ConsumeWineAction.AddBrand(it)) },
                    onDismissRequest = { onAction(ConsumeWineAction.DismissAddDialog) }
                )
            }
            is WineAddDialogType.AddWine -> {
                AddWineDialog(
                    state = addDialog.addDialogState,
                    submitNewWine = { s, wb, ws ->
                        onAction(
                            ConsumeWineAction.AddWine(s, wb, ws)
                        )
                    },
                    onDismissRequest = { onAction(ConsumeWineAction.DismissAddDialog) }
                )
            }
            null -> {}
        }
    }
}