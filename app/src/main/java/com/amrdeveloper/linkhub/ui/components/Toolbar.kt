package com.amrdeveloper.linkhub.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkhubToolbar(
    navController: NavController
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

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
                            if (expanded ) {
                                IconButton(
                                    onClick = {
                                        if (searchQuery.isNotEmpty()) searchQuery = ""
                                        else expanded = false
                                    },
                                    content =  {
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
                content = {}
            )

            if (!expanded) {
                OptionsMenuWithDropDownActions(navController = navController)
            }
        }
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

            HorizontalDivider()

//            TODO: Decide if we want to use Links and Home for show all links
//            DropdownMenuItem(
//                text = { Text("Links") },
//                leadingIcon = {
//                    Icon(
//                        painter = painterResource(R.drawable.ic_link),
//                        contentDescription = "Links",
//                        tint = Color.Unspecified
//                    )
//                },
//                onClick = {
//                    navController.navigate(R.id.action_homeFragment_to_linkListFragment)
//                    expanded = false
//                }
//            )

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

            HorizontalDivider()

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