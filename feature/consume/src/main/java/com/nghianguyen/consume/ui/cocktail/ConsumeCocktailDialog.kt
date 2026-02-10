package com.nghianguyen.consume.ui.cocktail

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
import com.nghianguyen.consume.ui.ExposedDropdownMenuField
import com.nghianguyen.consume.viewmodel.ConsumeCocktailViewModel
import com.nghianguyen.consume.viewmodel.ConsumeDrinkEvent
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailAction
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailDialogState
import com.nghianguyen.consume.viewmodel.cocktail.ConsumeCocktailEvent
import com.nghianguyen.text.toStringText
import com.nghianguyen.theme.LocalSpacing
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
        state = uiState,
        event = uiEvent,
        onAction = { viewModel.handleAction(it) },
//        onLiquorSelected = { viewModel.handleAction(ConsumeCocktailAction.LiquorSelected(it)) },
//        onCocktailSelected = { viewModel.handleAction(ConsumeCocktailAction.CocktailSelected(it)) },
//        onSubmitNewCocktail = { n, l ->
//            viewModel.handleAction(
//                ConsumeCocktailAction.AddCocktail(
//                    n,
//                    l
//                )
//            )
//        },
//        onSubmitConsumedCocktail = { viewModel.handleAction(ConsumeCocktailAction.SubmitConsumedCocktail) },
        dismissDialog = dismissDrinkDialog
    )
}

@Composable
fun ConsumeCocktailDialog(
    modifier: Modifier = Modifier,
    state: ConsumeCocktailDialogState,
    event: SharedFlow<ConsumeDrinkEvent>,
    onAction: (ConsumeCocktailAction) -> Unit,
//    onLiquorSelected: (Liquor?) -> Unit,
//    onCocktailSelected: (Drink.Cocktail?) -> Unit,
//    onSubmitNewCocktail: (String, Liquor) -> Unit,
//    onSubmitConsumedCocktail: () -> Unit,
    dismissDialog: () -> Unit
) {
    CollectFlowEffect(event) {
        when (it) {
            ConsumeCocktailEvent.SubmitConsumeCocktailSuccess -> {
                dismissDialog()
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var liquorExpanded by remember { mutableStateOf(false) }
    var selectedLiquorText by remember(state.selectedLiquor) {
        mutableStateOf(
            state.selectedLiquor?.name ?: defaultText
        )
    }

    var cocktailExpanded by remember { mutableStateOf(false) }
    var selectedCocktailText by remember(state.selectedCocktail) {
        mutableStateOf(
            state.selectedCocktail?.name ?: defaultText
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
                    text = stringResource(R.string.title_add_cocktail),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(LocalSpacing.current.large))

                ExposedDropdownMenuField(
                    menuItems = state.liquors,
                    text = selectedLiquorText,
                    label = { Text(stringResource(R.string.label_liquor)) },
                    expanded = liquorExpanded,
                    onExpandedChange = { liquorExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        liquorExpanded = false
                        onAction(ConsumeCocktailAction.LiquorSelected(it))
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = state.cocktails,
                    text = selectedCocktailText,
                    label = { Text(stringResource(R.string.label_cocktail)) },
                    expanded = cocktailExpanded,
                    onExpandedChange = { cocktailExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        cocktailExpanded = false
                        onAction(ConsumeCocktailAction.CocktailSelected(it))
                    }
                )

                TextButton(
                    onClick = {
                        onAction(ConsumeCocktailAction.OpenAddCocktailDialog)
                    }
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
                        onClick = { onAction(ConsumeCocktailAction.SubmitConsumedCocktail) },
                        enabled = state.selectedCocktail != null
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
            is CocktailAddDialogType.AddCocktail -> {
                AddCocktailDialog(
                    state = addDialog.addDialogState,
                    submitNewCocktail = { n, l ->
                        onAction(ConsumeCocktailAction.AddCocktail(n, l))
                    },
                    onDismissRequest = { onAction(ConsumeCocktailAction.DismissAddDialog) }
                )
            }
            null -> {}
        }
    }
}