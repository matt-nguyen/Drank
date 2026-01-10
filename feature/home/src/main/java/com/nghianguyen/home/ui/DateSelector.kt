package com.nghianguyen.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.nghianguyen.common.ui.R
import com.nghianguyen.theme.LocalSpacing

@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    dateText: String = "",
    isToday: Boolean = false,
    onOpenDatePicker: () -> Unit = {},
    onPrevDate: () -> Unit = {},
    onNextDate: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onPrevDate) {
            Icon(
                painter = painterResource(R.drawable.chevron_backward_24px),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Button(
            onClick = onOpenDatePicker,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Icon(
                painter = painterResource(R.drawable.calendar_today_24px),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(end = LocalSpacing.current.xSmall)
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            modifier = Modifier.alpha(if (!isToday) 1f else 0f),
            onClick = { if (!isToday) onNextDate() }
        ) {
            Icon(
                painter = painterResource(R.drawable.chevron_forward_24px),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
