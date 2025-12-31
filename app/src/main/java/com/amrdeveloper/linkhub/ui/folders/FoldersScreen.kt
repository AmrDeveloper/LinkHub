package com.amrdeveloper.linkhub.ui.folders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.ui.components.FolderList
import com.amrdeveloper.linkhub.ui.components.FolderViewKind
import com.amrdeveloper.linkhub.ui.components.ShowItemsOptionsDropdownButton
import com.amrdeveloper.linkhub.ui.components.ShowOption

@Composable
fun FoldersScreen(
    viewModel: FolderListViewModel = viewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showItemsOption by remember { mutableStateOf(FolderViewKind.List) }

    Column {
        Row {
            Text(
                text = "Folders: ${uiState.data.size}",
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )

            val showOptions = listOf(
                ShowOption(FolderViewKind.List.name, R.drawable.ic_list),
                ShowOption(FolderViewKind.Grid.name, R.drawable.ic_grid)
            )

            ShowItemsOptionsDropdownButton(showOptions) { showOption ->
                showItemsOption = FolderViewKind.valueOf(showOption.literal)
            }
        }

        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            return
        }

        FolderList(
            folders = uiState.data,
            viewKind = showItemsOption,
            onClick = { folder ->
                viewModel.incrementFolderClickCount(folder)

                val bundle = bundleOf("folder" to folder)
                navController.navigate(
                    R.id.action_folderListFragment_to_linkListFragment,
                    bundle
                )
            },
            onLongClick = { folder ->
                val bundle = bundleOf("folder" to folder)
                navController.navigate(
                    R.id.action_folderListFragment_to_folderFragment,
                    bundle
                )
            }
        )
    }
}
