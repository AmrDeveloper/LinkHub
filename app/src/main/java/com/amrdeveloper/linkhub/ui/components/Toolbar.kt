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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.ui.search.SearchScreen
import com.amrdeveloper.linkhub.ui.search.SearchViewModel
import com.amrdeveloper.linkhub.util.UiPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkhubToolbar(
    viewModel: SearchViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController
) {
    var expanded by rememberSaveable { mutableStateOf(value =false) }

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

            SearchScreen(
                modifier = Modifier.weight(1f),
                viewModel = viewModel,
                uiPreferences = uiPreferences,
                navController = navController,
                onSearchExpandedChanged = { isExpanded ->
                    expanded = isExpanded
                }
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

            if (currentDestination?.id != R.id.folderListFragment) {
                DropdownMenuItem(
                    text = { Text("Explorer") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_link),
                            contentDescription = "Explorer",
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