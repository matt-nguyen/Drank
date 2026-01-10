package com.nghianguyen.consume.ui

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
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerAction
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerEvent
import com.nghianguyen.consume.viewmodel.beer.ConsumeBeerState
import com.nghianguyen.consume.viewmodel.ConsumeBeerViewModel
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
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
        consumeBeerState = uiState,
        consumeBeerEvent = uiEvent,
        onBeerStyleSelected = {
            viewModel.handleAction(ConsumeBeerAction.BeerStyleSelected(it))
        },
        onBeerBrandSelected = {
            viewModel.handleAction(ConsumeBeerAction.BeerBrandSelected(it))
        },
        onBeerSelected = {
            viewModel.handleAction(ConsumeBeerAction.BeerSelected(it))
        },
        onSubmitNewBeerBrand = {
            viewModel.handleAction(ConsumeBeerAction.AddBeerBrand(it))
        },
        onSubmitNewBeer = { bs, br, b ->
            viewModel.handleAction(ConsumeBeerAction.AddBeer(bs, br, b))
        },
        onSubmitConsumedBeer = {
            viewModel.handleAction(ConsumeBeerAction.SubmitConsumedBeer)
        },
        closeDrinkDialog = dismissDrinkDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumeBeerDialog(
    modifier: Modifier = Modifier,
    consumeBeerState: ConsumeBeerState,
    consumeBeerEvent: SharedFlow<ConsumeBeerEvent>,
    onBeerStyleSelected: (BeerStyle?) -> Unit,
    onBeerBrandSelected: (BeerBrand?) -> Unit,
    onBeerSelected: (Drink.Beer?) -> Unit,
    onSubmitNewBeerBrand: (String) -> Unit,
    onSubmitNewBeer: (BeerStyle, BeerBrand, String) -> Unit,
    onSubmitConsumedBeer: () -> Unit,
    closeDrinkDialog: () -> Unit
) {
    var addBrandDialog by remember { mutableStateOf(false) }
    var addBeerDialog by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }
    var addDialogErrorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        consumeBeerEvent.collect {
            when (it) {
                ConsumeBeerEvent.AddBeerBrandSuccess -> {
                    addBrandDialog = false
                    addDialogErrorMsg = null
                }
                is ConsumeBeerEvent.AddBeerBrandError -> {
                    addDialogErrorMsg = it.errorMsg
                }
                ConsumeBeerEvent.AddBeerSuccess -> {
                    addBeerDialog = false
                    addDialogErrorMsg = null
                }
                is ConsumeBeerEvent.AddBeerError -> {
                    addDialogErrorMsg = it.errorMsg
                }
                ConsumeBeerEvent.SubmitConsumedBeerSuccess -> {
                    closeDrinkDialog()
                }
                is ConsumeBeerEvent.SubmitConsumedBeerError -> {
                    errorMsg = it.errorMsg
                }
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var beerStyleExpanded by remember { mutableStateOf(false) }
    var selectedBeerStyleText by remember(consumeBeerState.selectedStyle) {
        mutableStateOf(
            consumeBeerState.selectedStyle?.name ?: defaultText
        )
    }

    var beerBrandExpanded by remember { mutableStateOf(false) }
    var selectedBeerBrandText by remember(consumeBeerState.selectedBrand) {
        mutableStateOf(
            consumeBeerState.selectedBrand?.name ?: defaultText
        )
    }

    var beerExpanded by remember { mutableStateOf(false) }
    var selectedBeerText by remember(consumeBeerState.selectedBeer) {
        mutableStateOf(
            consumeBeerState.selectedBeer?.name ?: defaultText
        )
    }

    Dialog(
        onDismissRequest = closeDrinkDialog
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
                    text = stringResource(R.string.title_add_beer),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(32.dp))

                ExposedDropdownMenuField(
                    menuItems = consumeBeerState.beerStyles,
                    text = selectedBeerStyleText,
                    label = { Text(stringResource(R.string.label_style)) },
                    expanded = beerStyleExpanded,
                    onExpandedChange = { beerStyleExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerStyleExpanded = false
                        onBeerStyleSelected(it)
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = consumeBeerState.beerBrands,
                    text = selectedBeerBrandText,
                    label = { Text(stringResource(R.string.label_brand)) },
                    expanded = beerBrandExpanded,
                    onExpandedChange = { beerBrandExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerBrandExpanded = false
                        onBeerBrandSelected(it)
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
                    menuItems = consumeBeerState.beers,
                    text = selectedBeerText,
                    label = { Text(stringResource(R.string.label_beer)) },
                    expanded = beerExpanded,
                    onExpandedChange = { beerExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerExpanded = false
                        onBeerSelected(it)
                    }
                )

                TextButton(
                    onClick = { addBeerDialog = true }
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
                    TextButton(onClick = closeDrinkDialog) {
                        Text(
                            text = stringResource(R.string.button_cancel),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick = onSubmitConsumedBeer,
                        enabled = consumeBeerState.selectedBeer != null
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
                submitNewBrand = onSubmitNewBeerBrand,
                onDismissRequest = {
                    addBrandDialog = false
                    addDialogErrorMsg = null
                },
                errorMsg = addDialogErrorMsg
            )
        }

        if (addBeerDialog) {
            AddBeerDialog(
                beerStyles = consumeBeerState.beerStyles,
                beerBrands = consumeBeerState.beerBrands,
                submitNewBeer = onSubmitNewBeer,
                onDismissRequest = {
                    addBeerDialog = false
                    addDialogErrorMsg = null
                },
                errorMsg = addDialogErrorMsg
            )
        }

    }
}