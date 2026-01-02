package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.ui.SearchViewModel
import com.amrdeveloper.linkhub.util.UiPreferences

data class SearchParams(
    val isLinksSelected: Boolean = true,
    val isFoldersSelected: Boolean = true,
    val isPinnedSelected: Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkhubToolbar(
    viewModel: SearchViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController
) {
    var searchQuery by rememberSaveable { mutableStateOf(value = "") }
    var searchSelectionParams by remember { mutableStateOf(value = SearchParams())}
    var expanded by rememberSaveable { mutableStateOf(value =false) }

    LaunchedEffect(searchQuery, searchSelectionParams) {
        viewModel.updateSearchParams(query = searchQuery, params = searchSelectionParams)
    }

    val folders = viewModel.sortedFoldersState.collectAsStateWithLifecycle()
    val links = viewModel.sortedLinksState.collectAsStateWithLifecycle()

    Surface(
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 3.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!expanded) {
                Icon(
                    painter = painterResource(R.drawable.ic_link),
                    contentDescription = "LinkHub",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(22.dp)
                        .weight(0.2f)
                )
            }

            SearchBar(
                modifier = Modifier
                    .semantics { traversalIndex = 0f }
                    .weight(1f),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchQuery,
                        onQueryChange = {
                            searchQuery = it
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
                                        if (searchQuery.isNotEmpty()) searchQuery = ""
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
                onExpandedChange = { expanded = it },
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

                                },
                                onLongClick = { folder ->

                                }
                            )
                        }

                        if (searchSelectionParams.isLinksSelected) {
                            LinkList(
                                links = links.value.data,
                                onClick = { link ->

                                },
                                onLongClick = { link ->

                                },
                                showClickCount = uiPreferences.isClickCounterEnabled()
                            )
                        }
                    }
                }
            )

            if (!expanded) {
                OptionsMenuWithDropDownActions(navController = navController)
            }
        }
    }
}

@Composable
private fun SearchSelectionOptions(onSearchOptionsChanged: (SearchParams) -> Unit = {}) {
    var isLinksSelected by remember { mutableStateOf(value = true) }
    var isFoldersSelected by remember { mutableStateOf(value = true) }
    var isPinnedSelected by remember { mutableStateOf(value = false) }

    val constructSearchSelectionParams = {
        SearchParams(
            isLinksSelected = isLinksSelected,
            isFoldersSelected = isFoldersSelected,
            isPinnedSelected = isPinnedSelected
        )
    }

    Row(modifier = Modifier.fillMaxWidth()) {
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
                isPinnedSelected = !isPinnedSelected
                onSearchOptionsChanged(constructSearchSelectionParams())
            },
            label = { Text(text = "Pinned") },
            selected = isPinnedSelected,
            leadingIcon = {
                if (isPinnedSelected) {
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
    }
}

@Composable
private fun OptionsMenuWithDropDownActions(navController: NavController) {
    var expanded by remember { mutableStateOf(false) }
    val currentDestination = navController.currentDestination
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(R.drawable.ic_options),
                contentDescription = "Options",
                tint = Color.Unspecified
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("New Link") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_link),
                        contentDescription = "New Link",
                        tint = Color.Unspecified
                    )
                },
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_linkFragment)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("New Folder") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_folders),
                        contentDescription = "New Folder",
                        tint = Color.Unspecified
                    )
                },
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_folderFragment)
                    expanded = false
                }
            )

            if (currentDestination?.id != R.id.folderListFragment) {
                DropdownMenuItem(
                    text = { Text("Links") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_link),
                            contentDescription = "Links",
                            tint = Color.Unspecified
                        )
                    },
                    onClick = {
                        navController.navigate(R.id.action_homeFragment_to_linkListFragment)
                        expanded = false
                    }
                )
            }

            if (currentDestination?.id != R.id.folderListFragment) {
                DropdownMenuItem(
                    text = { Text("Folders") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_folders),
                            contentDescription = "Folders",
                            tint = Color.Unspecified
                        )
                    },
                    onClick = {
                        navController.navigate(R.id.action_homeFragment_to_folderListFragment)
                        expanded = false
                    }
                )
            }

            DropdownMenuItem(
                text = { Text("Settings") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = "Settings",
                        tint = Color.Unspecified
                    )
                },
                onClick = {
                    navController.navigate(R.id.action_homeFragment_to_settingFragment)
                    expanded = false
                }
            )
        }
    }
}