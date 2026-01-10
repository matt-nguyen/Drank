package com.nghianguyen.consume.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import com.nghianguyen.common.ui.R
import com.nghianguyen.drinks.model.Liquor

/**
 * Dialog for the user to add a new [Drink.Shot].
 */
@Composable
fun AddShotDialog(
    liquors: List<Liquor>,
    submitNewShot: (String, Liquor) -> Unit,
    onDismissRequest: () -> Unit,
    errorMsg: String? = null
) {
    val defaultText = stringResource(R.string.default_select)

    var liquorExpanded by remember { mutableStateOf(false) }
    var liquorSelected by remember { mutableStateOf<Liquor?>(null) }
    var selectedLiquorText by remember(liquorSelected) {
        mutableStateOf(
            liquorSelected?.name ?: defaultText
        )
    }

    var shotNameTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                ExposedDropdownMenuField(
                    menuItems = liquors,
                    text = selectedLiquorText,
                    label = { Text(stringResource(R.string.label_liquor)) },
                    expanded = liquorExpanded,
                    onExpandedChange = { liquorExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        liquorExpanded = false
                        liquorSelected = it
                    }
                )

                OutlinedTextField(
                    value = shotNameTextFieldValue,
                    onValueChange = { shotNameTextFieldValue = it },
                    label = {
                        Text(
                            text = stringResource(R.string.hint_enter_shot_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                errorMsg?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                TextButton(
                    onClick = {
                        submitNewShot(
                            shotNameTextFieldValue.text,
                            liquorSelected!!
                        )
                    },
                    enabled = liquorSelected != null && shotNameTextFieldValue.text.trim()
                        .isNotEmpty()
                ) {
                    Text(
                        text = stringResource(R.string.button_add),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(R.string.button_cancel),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

}