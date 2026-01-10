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
import com.nghianguyen.drinks.model.wine.WineBrand
import com.nghianguyen.drinks.model.wine.WineStyle

/**
 * Dialog for the user to add a new [Drink.Wine].
 */
@Composable
fun AddWineDialog(
    wineStyles: List<WineStyle>,
    wineBrands: List<WineBrand>,
    submitNewWine: (String, WineBrand, WineStyle) -> Unit,
    onDismissRequest: () -> Unit,
    errorMsg: String? = null
) {
    val defaultText = stringResource(R.string.default_select)

    var wineStyleExpanded by remember { mutableStateOf(false) }
    var wineStyleSelected by remember { mutableStateOf<WineStyle?>(null) }
    var selectedWineStyleText by remember(wineStyleSelected) {
        mutableStateOf(
            wineStyleSelected?.name ?: defaultText
        )
    }

    var wineBrandExpanded by remember { mutableStateOf(false) }
    var wineBrandSelected by remember { mutableStateOf<WineBrand?>(null) }
    var selectedWineBrandText by remember(wineBrandSelected) {
        mutableStateOf(
            wineBrandSelected?.name ?: defaultText
        )
    }

    var wineNameTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                ExposedDropdownMenuField(
                    menuItems = wineStyles,
                    text = selectedWineStyleText,
                    label = { Text(stringResource(R.string.label_style)) },
                    expanded = wineStyleExpanded,
                    onExpandedChange = { wineStyleExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineStyleExpanded = false
                        wineStyleSelected = it
                    }
                )

                ExposedDropdownMenuField(
                    menuItems = wineBrands,
                    text = selectedWineBrandText,
                    label = { Text(stringResource(R.string.label_brand)) },
                    expanded = wineBrandExpanded,
                    onExpandedChange = { wineBrandExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        wineBrandExpanded = false
                        wineBrandSelected = it
                    }
                )


                OutlinedTextField(
                    value = wineNameTextFieldValue,
                    onValueChange = { wineNameTextFieldValue = it },
                    label = {
                        Text(
                            text = stringResource(R.string.hint_enter_wine_name),
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
                        submitNewWine(
                            wineNameTextFieldValue.text,
                            wineBrandSelected!!,
                            wineStyleSelected!!
                        )
                    },
                    enabled = wineStyleSelected != null && wineBrandSelected != null && wineNameTextFieldValue.text.trim()
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