package com.nghianguyen.consume.ui

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nghianguyen.common.ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ExposedDropdownMenuField(
    menuItems: List<T>,
    text: String,
    label: @Composable () -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    getMenuItemName: (T) -> String,
    onMenuItemClick: (T?) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable),
            value = text,
            onValueChange = { /* readOnly */ },
            label = label,
            readOnly = true,
            textStyle = MaterialTheme.typography.labelSmall
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.default_select)) },
                onClick = { onMenuItemClick(null) }
            )

            menuItems.forEach { menuItem ->
                val menuItemName = getMenuItemName(menuItem)
                DropdownMenuItem(
                    text = { Text(menuItemName) },
                    onClick = { onMenuItemClick(menuItem) }
                )
            }
        }
    }

}