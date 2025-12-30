package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.R

@Composable
fun SaveDeleteActionsRow(
    onSaveActionClick: () -> Unit = {},
    onDeleteActionClick: () -> Unit = {}
) {
    Row(modifier = Modifier.padding(5.dp).fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        OutlinedIconButton(
            border = BorderStroke(1.dp, colorResource(R.color.light_blue_600)),
            onClick = onSaveActionClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_save),
                contentDescription = "Save",
                tint = colorResource(R.color.light_blue_600)
            )
        }

        OutlinedIconButton(
            border = BorderStroke(1.dp, colorResource(R.color.red)),
            onClick = onDeleteActionClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = "Delete",
                tint = colorResource(R.color.red)
            )
        }
    }
}