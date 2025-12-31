package com.amrdeveloper.linkhub.ui.links

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.components.LinkActionsBottomSheet
import com.amrdeveloper.linkhub.ui.components.LinkList
import com.amrdeveloper.linkhub.util.UiPreferences

@Composable
fun LinksScreen(
    currentFolder: Folder,
    viewModel: LinkListViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController,
) {
    var lastClickedLink by remember { mutableStateOf<Link?>(value = null) }
    var showLinkActionsDialog by remember { mutableStateOf(value = false) }

    LaunchedEffect(true) {
        viewModel.updateFolderId(currentFolder.id)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        FolderHeader(currentFolder)

        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            return
        }

        LinkList(
            links = uiState.links,
            onClick = { link ->
                viewModel.incrementLinkClickCount(link)
                lastClickedLink = link
                showLinkActionsDialog = true
            },
            onLongClick = { link ->
                val bundle = bundleOf("link" to link)
                navController.navigate(
                    R.id.action_linkListFragment_to_linkFragment,
                    bundle
                )
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

@Composable
private fun FolderHeader(folder: Folder) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = folder.folderColor.drawableId),
            contentDescription = "Folder Icon",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "/${folder.name}")
    }
}