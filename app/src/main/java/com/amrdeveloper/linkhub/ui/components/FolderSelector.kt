package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amrdeveloper.linkhub.data.Folder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderSelector(
    selectedFolder: Folder,
    folders: List<Folder>,
    onFolderSelected: (Folder) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {

        OutlinedTextField(
            value = selectedFolder.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Folder (Optional") },
            leadingIcon = {
                Icon(
                    painter = painterResource(selectedFolder.folderColor.drawableId),
                    contentDescription = "Folder Icon",
                    tint = Color.Unspecified
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            folders.forEach { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { folder ->
                        expanded = false
                        onFolderSelected(folder)
                    },
                    folderItemElevation = 0.dp
                )
            }
        }
    }
}
