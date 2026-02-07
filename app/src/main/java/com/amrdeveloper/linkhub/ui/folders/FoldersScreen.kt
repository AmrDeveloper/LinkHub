package com.amrdeveloper.linkhub.ui.folders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.FolderSortingOption
import com.amrdeveloper.linkhub.ui.components.FolderList
import com.amrdeveloper.linkhub.ui.components.FolderViewKind
import com.amrdeveloper.linkhub.ui.components.LinkhubToolbar
import com.amrdeveloper.linkhub.ui.components.ShowItemsOptionsDropdownButton
import com.amrdeveloper.linkhub.ui.components.ShowOption
import com.amrdeveloper.linkhub.util.UiPreferences

private val showOptions = listOf(
    ShowOption(FolderViewKind.List.name, R.drawable.ic_list),
    ShowOption(FolderViewKind.Grid.name, R.drawable.ic_grid)
)

private val sortingOptions = listOf(
    ShowOption("Default", R.drawable.ic_sorting),
    ShowOption("Name A-Z", R.drawable.ic_sort_az),
    ShowOption("Name Z-A", R.drawable.ic_sort_za),
    ShowOption("Clicks Asc", R.drawable.ic_sort_12),
    ShowOption("Clicks Desc", R.drawable.ic_sort_21),
)

@Composable
fun FoldersScreen(
    viewModel: FolderListViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showItemsOption by remember { mutableStateOf(FolderViewKind.List) }
    var sortItemsOption by remember { mutableStateOf(FolderSortingOption.DEFAULT) }

    LaunchedEffect(sortItemsOption) {
        viewModel.setSortingOption(sortItemsOption)
    }

    Scaffold(topBar = { LinkhubToolbar(viewModel(), uiPreferences, navController) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row {
                Text(
                    text = "Folders",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.light_blue_600),
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f)
                )

                ShowItemsOptionsDropdownButton(sortingOptions) { index ->
                    sortItemsOption = when (index) {
                        0 -> FolderSortingOption.DEFAULT
                        1 -> FolderSortingOption.NAME_ASC
                        2 -> FolderSortingOption.NAME_DESC
                        3 -> FolderSortingOption.CLICK_COUNT_ASC
                        4 -> FolderSortingOption.CLICK_COUNT_DESC
                        else -> FolderSortingOption.DEFAULT
                    }
                }

                ShowItemsOptionsDropdownButton(showOptions) { index ->
                    showItemsOption = FolderViewKind.valueOf(showOptions[index].literal)
                }
            }

            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                return@Column
            }

            FolderList(
                folders = uiState.data,
                viewKind = showItemsOption,
                onClick = { folder ->
                    viewModel.incrementFolderClickCount(folder)

                    val bundle = bundleOf("folder" to folder)
                    navController.navigate(
                        R.id.linkListFragment,
                        bundle
                    )
                },
                onLongClick = { folder ->
                    val bundle = bundleOf("folder" to folder)
                    navController.navigate(
                        R.id.folderFragment,
                        bundle
                    )
                },
                minimalModeEnabled = uiPreferences.isMinimalModeEnabled()
            )
        }

    }
}
