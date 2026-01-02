package com.amrdeveloper.linkhub.ui.search

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.components.FolderList
import com.amrdeveloper.linkhub.ui.components.FolderViewKind
import com.amrdeveloper.linkhub.ui.components.LinkActionsBottomSheet
import com.amrdeveloper.linkhub.ui.components.LinkList
import com.amrdeveloper.linkhub.util.UiPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController,
    onSearchExpandedChanged: (Boolean) -> Unit = {}
) {
    var searchSelectionParams by remember { mutableStateOf(value = SearchParams())}
    var expanded by rememberSaveable { mutableStateOf(value =false) }

    var lastClickedLink by remember { mutableStateOf<Link?>(value = null) }
    var showLinkActionsDialog by remember { mutableStateOf(value = false) }

    LaunchedEffect(searchSelectionParams) {
        viewModel.updateSearchParams(params = searchSelectionParams)
    }

    LaunchedEffect(expanded) {
        onSearchExpandedChanged(expanded)
    }

    val folders = viewModel.sortedFoldersState.collectAsStateWithLifecycle()
    val links = viewModel.sortedLinksState.collectAsStateWithLifecycle()

    SearchBar(
        modifier = modifier.semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = searchSelectionParams.query,
                onQueryChange = {
                    searchSelectionParams.query = it
                },
                onSearch = {
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                },
                placeholder = {
                    Text(
                        text = "Search",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.Unspecified,
                    )
                },
                trailingIcon = {
                    if (expanded) {
                        IconButton(
                            onClick = {
                                if (searchSelectionParams.query.isNotEmpty()) searchSelectionParams.query = ""
                                else expanded = false
                            },
                            content = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_delete),
                                    contentDescription = "Delete",
                                    tint = Color.Unspecified
                                )
                            }
                        )
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = {
            expanded = it
        },
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                SearchSelectionOptions {
                    searchSelectionParams = it
                }

                if (searchSelectionParams.isFoldersSelected) {
                    FolderList(
                        folders = folders.value.data,
                        viewKind = FolderViewKind.List,
                        onClick = { folder ->
                            viewModel.incrementFolderClickCount(folder)
                            val bundle = bundleOf("folder" to folder)
                            navController.navigate(R.id.action_homeFragment_to_linkListFragment, bundle)
                        },
                        onLongClick = { folder ->
                            val bundle = bundleOf("folder" to folder)
                            navController.navigate(R.id.action_homeFragment_to_folderFragment, bundle)
                        }
                    )
                }

                if (searchSelectionParams.isLinksSelected) {
                    LinkList(
                        links = links.value.data,
                        onClick = { link ->
                            viewModel.incrementLinkClickCount(link)
                            lastClickedLink = link
                            showLinkActionsDialog = true
                        },
                        onLongClick = { link ->
                            val bundle = bundleOf("link" to link)
                            navController.navigate(R.id.action_homeFragment_to_linkFragment, bundle)
                        },
                        showClickCount = uiPreferences.isClickCounterEnabled()
                    )
                }

                if (showLinkActionsDialog) {
                    lastClickedLink?.let { link ->
                        LinkActionsBottomSheet(link) {
                            showLinkActionsDialog = false
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun SearchSelectionOptions(onSearchOptionsChanged: (SearchParams) -> Unit = {}) {
    var isLinksSelected by remember { mutableStateOf(value = true) }
    var isFoldersSelected by remember { mutableStateOf(value = true) }
    var isPinnedSelected by remember { mutableStateOf<Boolean?>(value = null) }
    var isClickedSelected by remember { mutableStateOf<Boolean?>(value = null) }

    val constructSearchSelectionParams = {
        SearchParams(
            isLinksSelected = isLinksSelected,
            isFoldersSelected = isFoldersSelected,
            isPinnedSelected = if (isPinnedSelected == true) true else null,
            isClickedSelected = if (isClickedSelected == true) true else null
        )
    }

    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
        FilterChip(
            onClick = {
                isLinksSelected = !isLinksSelected
                onSearchOptionsChanged(constructSearchSelectionParams())
            },
            label = { Text(text = "Links") },
            selected = isLinksSelected,
            leadingIcon = {
                if (isLinksSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Select links icon",
                        tint = colorResource(R.color.light_blue_600),
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            },
            modifier = Modifier.padding(4.dp),
        )

        FilterChip(
            onClick = {
                isFoldersSelected = !isFoldersSelected
                onSearchOptionsChanged(constructSearchSelectionParams())
            },
            label = { Text(text = "Folders") },
            selected = isFoldersSelected,
            leadingIcon = {
                if (isFoldersSelected) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Select folders icon",
                        tint = colorResource(R.color.light_blue_600),
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            },
            modifier = Modifier.padding(4.dp)
        )

        FilterChip(
            onClick = {
                isPinnedSelected = !(isPinnedSelected ?: false)
                onSearchOptionsChanged(constructSearchSelectionParams())
            },
            label = { Text(text = "Pinned") },
            selected = isPinnedSelected == true,
            leadingIcon = {
                if (isPinnedSelected == true) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Select pinned icon",
                        tint = colorResource(R.color.light_blue_600),
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            },
            modifier = Modifier.padding(4.dp)
        )

        FilterChip(
            onClick = {
                isClickedSelected = !(isClickedSelected ?: false)
                onSearchOptionsChanged(constructSearchSelectionParams())
            },
            label = { Text(text = "Clicked before") },
            selected = isClickedSelected == true,
            leadingIcon = {
                if (isClickedSelected == true) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = "Select Clicked icon",
                        tint = colorResource(R.color.light_blue_600),
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            },
            modifier = Modifier.padding(4.dp)
        )
    }
}
