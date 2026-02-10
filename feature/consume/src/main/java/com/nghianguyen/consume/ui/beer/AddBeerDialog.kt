package com.nghianguyen.consume.ui.beer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import com.nghianguyen.common.ui.R
import com.nghianguyen.consume.ui.ExposedDropdownMenuField
import com.nghianguyen.drinks.model.beer.BeerBrand
import com.nghianguyen.drinks.model.beer.BeerStyle
import com.nghianguyen.text.toStringText

/**
 * Dialog for the user to add a new [Drink.Beer].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBeerDialog(
    state: AddBeerDialogState,
    submitNewBeer: (BeerStyle, BeerBrand, String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val defaultText = stringResource(R.string.default_select)

    var beerStyleExpanded by remember { mutableStateOf(false) }
    var beerStyleSelected by remember { mutableStateOf<BeerStyle?>(null) }
    var selectedBeerStyleText by remember(beerStyleSelected) {
        mutableStateOf(
            beerStyleSelected?.name ?: defaultText
        )
    }

    var beerBrandExpanded by remember { mutableStateOf(false) }
    var beerBrandSelected by remember { mutableStateOf<BeerBrand?>(null) }
    var selectedBeerBrandText by remember(beerBrandSelected) {
        mutableStateOf(
            beerBrandSelected?.name ?: defaultText
        )
    }

    var beerNameTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                ExposedDropdownMenuField(
                    menuItems = state.beerStyles,
                    text = selectedBeerStyleText,
                    label = { Text(stringResource(R.string.label_style)) },
                    expanded = beerStyleExpanded,
                    onExpandedChange = { beerStyleExpanded = it },
                    getMenuItemName = { it.name },
                    onMenuItemClick = {
                        beerStyleExpanded = false
                        beerStyleSelected = it
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
                        beerBrandSelected = it
                    }
                )


                OutlinedTextField(
                    value = beerNameTextFieldValue,
                    onValueChange = { beerNameTextFieldValue = it },
                    label = {
                        Text(
                            text = stringResource(R.string.hint_enter_beer_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                state.errorMsg?.toStringText(LocalContext.current)?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                TextButton(
                    onClick = {
                        submitNewBeer(
                            beerStyleSelected!!,
                            beerBrandSelected!!,
                            beerNameTextFieldValue.text
                        )
                    },
                    enabled = beerStyleSelected != null && beerBrandSelected != null && beerNameTextFieldValue.text.trim()
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
