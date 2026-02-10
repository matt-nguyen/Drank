package com.nghianguyen.consume.ui.shot

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
import com.nghianguyen.consume.viewmodel.ConsumeShotViewModel
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotAction
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotDialogState
import com.nghianguyen.consume.viewmodel.shot.ConsumeShotEvent
import com.nghianguyen.text.toStringText
import com.nghianguyen.theme.LocalSpacing
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
        state = uiState,
        event = uiEvent,
        onAction = { viewModel.handleAction(it) },
        dismissDialog = dismissDrinkDialog
    )
}

@Composable
fun ConsumeShotDialog(
    modifier: Modifier = Modifier,
    state: ConsumeShotDialogState,
    event: SharedFlow<ConsumeShotEvent>,
    onAction: (ConsumeShotAction) -> Unit,
    dismissDialog: () -> Unit
) {
    CollectFlowEffect(event) {
        when (it) {
            ConsumeShotEvent.SubmitConsumeShotSuccess -> {
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

    var shotExpanded by remember { mutableStateOf(false) }
    var selectedShotText by remember(state.selectedShot) {
        mutableStateOf(
            state.selectedShot?.name ?: defaultText
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
                    text = stringResource(R.string.title_add_shot),
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
                        onAction(ConsumeShotAction.LiquorSelected(it))
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = state.shots,
                    text = selectedShotText,
                    label = { Text(stringResource(R.string.label_shot)) },
                    expanded = shotExpanded,
                    onExpandedChange = { shotExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        shotExpanded = false
                        onAction(ConsumeShotAction.ShotSelected(it))
                    }
                )

                TextButton(
                    onClick = { onAction(ConsumeShotAction.OpenAddShotDialog) }
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
                        onClick = { onAction(ConsumeShotAction.SubmitConsumedShot) },
                        enabled = state.selectedShot != null
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
            is ShotAddDialogType.AddShot -> {
                AddShotDialog(
                    state = addDialog.addDialogState,
                    submitNewShot = { s, l ->
                        onAction(ConsumeShotAction.AddShot(s, l))
                    },
                    onDismissRequest = {
                        onAction(ConsumeShotAction.DismissAddDialog)
                    }
                )
            }
            null -> {}
        }

    }
}