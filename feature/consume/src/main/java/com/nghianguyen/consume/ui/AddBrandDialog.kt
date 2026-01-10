package com.nghianguyen.consume.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import com.nghianguyen.common.ui.R
import kotlinx.coroutines.flow.first

/**
 * Dialog for the user to add a new brand.
 */
@Composable
fun AddBrandDialog(
    submitNewBrand: (String) -> Unit,
    onDismissRequest: () -> Unit,
    errorMsg: String? = null
) {

    val focusRequester = remember { FocusRequester() }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        var brandNameTextFieldValue by remember { mutableStateOf(TextFieldValue("")) }
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column {
                OutlinedTextField(
                    value = brandNameTextFieldValue,
                    onValueChange = { brandNameTextFieldValue = it },
                    label = {
                        Text(
                            text = stringResource(R.string.hint_enter_brand_name),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    modifier = Modifier.focusRequester(focusRequester)
                )
                errorMsg?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                TextButton(
                    onClick = { submitNewBrand(brandNameTextFieldValue.text) },
                    enabled = brandNameTextFieldValue.text.isNotEmpty()
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

    // https://stackoverflow.com/questions/69750447/jetpack-compose-focus-requester-not-working-with-dialog
    val windowInfo = LocalWindowInfo.current
    LaunchedEffect(Unit) {
        val readyToFocus = !snapshotFlow { windowInfo.isWindowFocused }
            .first { !it }

        if (readyToFocus) {
            focusRequester.requestFocus()
        }
    }
}
