package com.nghianguyen.consume.ui.beer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.nghianguyen.consume.viewmodel.ConsumeBeerViewModel
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerAction
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerDialogState
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerEvent
import com.nghianguyen.text.toStringText
import com.nghianguyen.theme.LocalSpacing
import kotlinx.coroutines.flow.SharedFlow

/**
 * Dialog for the user to enter info on a beer being consumed.
 */
@Composable
fun ConsumeBeerDialog(
    viewModel: ConsumeBeerViewModel,
    dismissDrinkDialog: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent

    ConsumeBeerDialog(
        state = uiState,
        event = uiEvent,
        onAction = { viewModel.handleAction(it) },
        dismissDialog = dismissDrinkDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumeBeerDialog(
    modifier: Modifier = Modifier,
    state: ConsumeBeerDialogState,
    event: SharedFlow<ConsumeBeerEvent>,
    onAction: (ConsumeBeerAction) -> Unit,
    dismissDialog: () -> Unit
) {
    CollectFlowEffect(event) {
        when (it) {
            ConsumeBeerEvent.SubmitConsumedBeerSuccess -> {
                dismissDialog()
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var beerStyleExpanded by remember { mutableStateOf(false) }
    var selectedBeerStyleText by remember(state.selectedStyle) {
        mutableStateOf(
            state.selectedStyle?.name ?: defaultText
        )
    }

    var beerBrandExpanded by remember { mutableStateOf(false) }
    var selectedBeerBrandText by remember(state.selectedBrand) {
        mutableStateOf(
            state.selectedBrand?.name ?: defaultText
        )
    }

    var beerExpanded by remember { mutableStateOf(false) }
    var selectedBeerText by remember(state.selectedBeer) {
        mutableStateOf(
            state.selectedBeer?.name ?: defaultText
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
                    text = stringResource(R.string.title_add_beer),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(LocalSpacing.current.large))

                ExposedDropdownMenuField(
                    menuItems = state.beerStyles,
                    text = selectedBeerStyleText,
                    label = { Text(stringResource(R.string.label_style)) },
                    expanded = beerStyleExpanded,
                    onExpandedChange = { beerStyleExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerStyleExpanded = false
                        onAction(ConsumeBeerAction.StyleSelected(it))
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = state.beerBrands,
                    text = selectedBeerBrandText,
                    label = { Text(stringResource(R.string.label_brand)) },
                    expanded = beerBrandExpanded,
                    onExpandedChange = { beerBrandExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerBrandExpanded = false
                        onAction(ConsumeBeerAction.BrandSelected(it))
                    }
                )

                TextButton(
                    onClick = { onAction(ConsumeBeerAction.OpenAddBrandDialog) }
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
                    menuItems = state.beers,
                    text = selectedBeerText,
                    label = { Text(stringResource(R.string.label_beer)) },
                    expanded = beerExpanded,
                    onExpandedChange = { beerExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerExpanded = false
                        onAction(ConsumeBeerAction.BeerSelected(it))
                    }
                )

                TextButton(
                    onClick = { onAction(ConsumeBeerAction.OpenAddBeerDialog) }
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
                        onClick = { onAction(ConsumeBeerAction.SubmitConsumedBeer) },
                        enabled = state.selectedBeer != null
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
            is BeerAddDialogType.AddBrand -> {
                AddBrandDialog(
                    state = addDialog.addDialogState,
                    submitNewBrand = { onAction(ConsumeBeerAction.AddBrand(it)) },
                    onDismissRequest = { onAction(ConsumeBeerAction.DismissAddDialog) }
                )
            }

            is BeerAddDialogType.AddBeer -> {
                AddBeerDialog(
                    state = addDialog.addDialogState,
                    submitNewBeer = { bs, bb, s ->
                        onAction(
                            ConsumeBeerAction.AddBeer(bs, bb, s)
                        )
                    },
                    onDismissRequest = { onAction(ConsumeBeerAction.DismissAddDialog) }
                )
            }

            null -> {}
        }
    }

}