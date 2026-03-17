package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

data class DropDownOption(
    val text: String,
    val icon: Int,
)

@Composable
fun DropdownContainer(
    options: List<DropDownOption>,
    onOptionSelected: (DropDownOption) -> Unit,
    anchorContent: @Composable (onMenuRequest: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(value = false) }

    Box {
        anchorContent { expanded = true }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.text) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = option.icon),
                            contentDescription = option.text,
                            tint = Color.Unspecified
                        )
                    }
                )
            }
        }
    }
}
