package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.R

@Composable
fun PinnedSwitch(isChecked: Boolean = false, onCheckedChange: (Boolean) -> Unit = {}) {
    var enabled by remember { mutableStateOf(isChecked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Pinned",
            modifier = Modifier.weight(1f),
            color = colorResource(R.color.light_blue_600),
            style = MaterialTheme.typography.titleMedium,
        )

        Switch(
            checked = enabled,
            onCheckedChange = {
                enabled = !enabled
                onCheckedChange(enabled)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(R.color.light_blue_900),
                checkedTrackColor = colorResource(R.color.light_blue_200),
            )
        )
    }
}