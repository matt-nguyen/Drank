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
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailAction
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailEvent
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailState
import com.nghianguyen.consume.viewmodel.ConsumeCocktailViewModel
import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import kotlinx.coroutines.flow.SharedFlow

/**
 * Dialog for the user to enter info on a cocktail being consumed.
 */
@Composable
fun ConsumeCocktailDialog(
    viewModel: ConsumeCocktailViewModel,
    dismissDrinkDialog: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent

    ConsumeCocktailDialog(
        consumeCocktailState = uiState,
        consumeCocktailEvent = uiEvent,
        onLiquorSelected = { viewModel.handleAction(ConsumeCocktailAction.LiquorSelected(it)) },
        onCocktailSelected = { viewModel.handleAction(ConsumeCocktailAction.CocktailSelected(it)) },
        onSubmitNewCocktail = { n, l ->
            viewModel.handleAction(
                ConsumeCocktailAction.AddCocktail(
                    n,
                    l
                )
            )
        },
        onSubmitConsumedCocktail = { viewModel.handleAction(ConsumeCocktailAction.SubmitConsumedCocktail) },
        dismissDialog = dismissDrinkDialog
    )
}

@Composable
fun ConsumeCocktailDialog(
    modifier: Modifier = Modifier,
    consumeCocktailState: ConsumeCocktailState,
    consumeCocktailEvent: SharedFlow<ConsumeDrinkEvent>,
    onLiquorSelected: (Liquor?) -> Unit,
    onCocktailSelected: (Drink.Cocktail?) -> Unit,
    onSubmitNewCocktail: (String, Liquor) -> Unit,
    onSubmitConsumedCocktail: () -> Unit,
    dismissDialog: () -> Unit
) {
    var addCocktailDialog by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }
    var addDialogErrorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        consumeCocktailEvent.collect {
            when (it) {
                ConsumeCocktailEvent.AddCocktailSuccess -> {
                    addCocktailDialog = false
                    addDialogErrorMsg = null
                }
                is ConsumeCocktailEvent.AddCocktailError -> {
                    addDialogErrorMsg = it.errorMsg
                }
                ConsumeCocktailEvent.SubmitConsumeCocktailSuccess -> {
                    dismissDialog()
                }
                is ConsumeCocktailEvent.SubmitConsumeCocktailError -> {
                    errorMsg = it.errorMsg
                }
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var liquorExpanded by remember { mutableStateOf(false) }
    var selectedLiquorText by remember(consumeCocktailState.selectedLiquor) {
        mutableStateOf(
            consumeCocktailState.selectedLiquor?.name ?: defaultText
        )
    }

    var cocktailExpanded by remember { mutableStateOf(false) }
    var selectedCocktailText by remember(consumeCocktailState.selectedCocktail) {
        mutableStateOf(
            consumeCocktailState.selectedCocktail?.name ?: defaultText
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
                    text = stringResource(R.string.title_add_cocktail),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(32.dp))

                ExposedDropdownMenuField(
                    menuItems = consumeCocktailState.liquors,
                    text = selectedLiquorText,
                    label = { Text(stringResource(R.string.label_liquor)) },
                    expanded = liquorExpanded,
                    onExpandedChange = { liquorExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        liquorExpanded = false
                        onLiquorSelected(it)
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = consumeCocktailState.cocktails,
                    text = selectedCocktailText,
                    label = { Text(stringResource(R.string.label_cocktail)) },
                    expanded = cocktailExpanded,
                    onExpandedChange = { cocktailExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        cocktailExpanded = false
                        onCocktailSelected(it)
                    }
                )

                TextButton(
                    onClick = { addCocktailDialog = true }
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
                        onClick = onSubmitConsumedCocktail,
                        enabled = consumeCocktailState.selectedCocktail != null
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

        if (addCocktailDialog) {
            AddCocktailDialog(
                liquors = consumeCocktailState.liquors,
                submitNewCocktail = onSubmitNewCocktail,
                onDismissRequest = {
                    addCocktailDialog = false
                    addDialogErrorMsg = null
                },
                errorMsg = addDialogErrorMsg
            )
        }
    }
}