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
import com.nghianguyen.consume.viewmodel.ConsumeShotViewModel
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotAction
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotEvent
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotState
import com.nghianguyen.drinks.model.Drink
import com.nghianguyen.drinks.model.Liquor
import kotlinx.coroutines.flow.SharedFlow

/**
 * Dialog for the user to enter info on a shot of liquor being consumed.
 */
@Composable
fun ConsumeShotDialog(
    viewModel: ConsumeShotViewModel,
    dismissDrinkDialog: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent

    ConsumeShotDialog(
        consumeShotState = uiState,
        consumeShotEvent = uiEvent,
        onLiquorSelected = { viewModel.handleAction(ConsumeShotAction.LiquorSelected(it)) },
        onShotSelected = { viewModel.handleAction(ConsumeShotAction.ShotSelected(it)) },
        onSubmitNewShot = { n, l -> viewModel.handleAction(ConsumeShotAction.AddShot(n, l)) },
        onSubmitConsumedShot = { viewModel.handleAction(ConsumeShotAction.SubmitConsumedShot) },
        dismissDialog = dismissDrinkDialog
    )
}

@Composable
fun ConsumeShotDialog(
    modifier: Modifier = Modifier,
    consumeShotState: ConsumeShotState,
    consumeShotEvent: SharedFlow<ConsumeShotEvent>,
    onLiquorSelected: (Liquor?) -> Unit,
    onShotSelected: (Drink.Shot?) -> Unit,
    onSubmitNewShot: (String, Liquor) -> Unit,
    onSubmitConsumedShot: () -> Unit,
    dismissDialog: () -> Unit
) {
    var addShotDialog by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf<String?>(null) }
    var addDialogErrorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        consumeShotEvent.collect {
            when (it) {
                is ConsumeShotEvent.AddShotError -> {
                    addDialogErrorMsg = it.errorMsg
                }

                ConsumeShotEvent.AddShotSuccess -> {
                    addShotDialog = false
                    addDialogErrorMsg = null
                }

                is ConsumeShotEvent.SubmitConsumeShotError -> {
                    errorMsg = it.errorMsg
                }

                ConsumeShotEvent.SubmitConsumeShotSuccess -> {
                    dismissDialog()
                }
            }
        }
    }

    val defaultText = stringResource(R.string.default_select)

    var liquorExpanded by remember { mutableStateOf(false) }
    var selectedLiquorText by remember(consumeShotState.selectedLiquor) {
        mutableStateOf(
            consumeShotState.selectedLiquor?.name ?: defaultText
        )
    }

    var shotExpanded by remember { mutableStateOf(false) }
    var selectedShotText by remember(consumeShotState.selectedShot) {
        mutableStateOf(
            consumeShotState.selectedShot?.name ?: defaultText
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
                    text = stringResource(R.string.title_add_shot),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(32.dp))

                ExposedDropdownMenuField(
                    menuItems = consumeShotState.liquors,
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
                    menuItems = consumeShotState.shots,
                    text = selectedShotText,
                    label = { Text(stringResource(R.string.label_shot)) },
                    expanded = shotExpanded,
                    onExpandedChange = { shotExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        shotExpanded = false
                        onShotSelected(it)
                    }
                )

                TextButton(
                    onClick = { addShotDialog = true }
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
                        onClick = onSubmitConsumedShot,
                        enabled = consumeShotState.selectedShot != null
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

        if (addShotDialog) {
            AddShotDialog(
                liquors = consumeShotState.liquors,
                submitNewShot = onSubmitNewShot,
                onDismissRequest = {
                    addShotDialog = false
                    addDialogErrorMsg = null
                },
                errorMsg = addDialogErrorMsg
            )
        }
    }
}