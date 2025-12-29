package com.amrdeveloper.linkhub.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.R

data class ShowOption(
    val literal: String,
    val icon: Int,
)

@Composable
fun ShowItemsOptionsDropdownButton(
    options: List<ShowOption>,
    selectedOptionIndex: Int = 0,
    onOptionSelected: (ShowOption) -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(selectedOptionIndex) }

    Box {
        IconButton(onClick = { isExpanded = true }) {
            Icon(
                painter = painterResource(options[selectedIndex].icon),
                contentDescription = options[selectedIndex].literal,
                tint = colorResource(R.color.light_blue_600),
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(option.icon),
                                contentDescription = option.literal,
                                tint = colorResource(R.color.light_blue_600),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option.literal)
                        }
                    },
                    onClick = {
                        onOptionSelected(option)
                        selectedIndex = index
                        isExpanded = false
                    },
                    trailingIcon = {
                        if (index == selectedIndex) {
                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = colorResource(R.color.light_blue_600),
                            )
                        }
                    }
                )
            }
        }
    }
}