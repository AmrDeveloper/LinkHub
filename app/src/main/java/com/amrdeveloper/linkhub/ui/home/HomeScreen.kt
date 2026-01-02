package com.amrdeveloper.linkhub.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.amrdeveloper.linkhub.ui.components.LinkhubToolbar
import com.amrdeveloper.linkhub.util.UiPreferences

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController
) {
    val folders = viewModel.mostUsedLimitedFoldersState.collectAsStateWithLifecycle()
    val links = viewModel.sortedLinksState.collectAsStateWithLifecycle()

    var lastClickedLink by remember { mutableStateOf<Link?>(value = null) }
    var showLinkActionsDialog by remember { mutableStateOf(value = false) }

    Scaffold(topBar = { LinkhubToolbar(viewModel(), uiPreferences, navController) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = "Most used Folders",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.light_blue_600)
            )

            FolderList(
                folders = folders.value.data,
                viewKind = FolderViewKind.Grid,
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                        navController.navigate(R.id.action_homeFragment_to_folderListFragment)
                    },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Show all",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.light_blue_600)
                )

                Icon(
                    painter = painterResource(R.drawable.ic_arrow_forward),
                    contentDescription = "Show all",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = "Links",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.light_blue_600)
            )

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

            if (showLinkActionsDialog) {
                lastClickedLink?.let { link ->
                    LinkActionsBottomSheet(link) {
                        showLinkActionsDialog = false
                    }
                }
            }
        }
    }
}