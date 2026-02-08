package com.amrdeveloper.linkhub.ui.explorer

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.components.FolderItem
import com.amrdeveloper.linkhub.ui.components.LinkActionsBottomSheet
import com.amrdeveloper.linkhub.ui.components.LinkItem
import com.amrdeveloper.linkhub.ui.components.LinkhubToolbar
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.openLinkIntent

@Composable
fun ExplorerScreen(
    currentFolder: Folder?,
    viewModel: ExplorerViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController,
) {
    val context = LocalContext.current
    var lastClickedLink by remember { mutableStateOf<Link?>(value = null) }
    var showLinkActionsDialog by remember { mutableStateOf(value = false) }

    LaunchedEffect(true) {
        currentFolder?.let { viewModel.updateFolderId(folderId = it.id) }
    }

    val sortedFoldersState by viewModel.sortedFoldersState.collectAsStateWithLifecycle()
    val sortedLinksState by viewModel.sortedLinksState.collectAsStateWithLifecycle()
    Scaffold(topBar = { LinkhubToolbar(viewModel(), uiPreferences, navController) }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                currentFolder?.let { FolderHeader(it) }
            }

            if (sortedFoldersState.isLoading || sortedLinksState.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                    )
                }
                return@LazyColumn
            }

            items(sortedFoldersState.data) { folder ->
                FolderItem(
                    folder = folder,
                    onClick = {
                        viewModel.incrementFolderClickCount(folder)
                        val bundle = bundleOf("folder" to folder)
                        navController.navigate(
                            R.id.linkListFragment,
                            bundle
                        )
                    },
                    onLongClick = {
                        val bundle = bundleOf("folder" to folder)
                        navController.navigate(
                            R.id.folderFragment,
                            bundle
                        )
                    },
                    minimalModeEnabled = uiPreferences.isMinimalModeEnabled(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }

            items(sortedLinksState.data) { link ->
                LinkItem(
                    link = link,
                    onClick = {
                        viewModel.incrementLinkClickCount(link)
                        lastClickedLink = link
                        if (uiPreferences.isOpenLinkByClickOptionEnabled()) {
                            try {
                                openLinkIntent(context = context, link = link.url)
                            } catch (_: Exception) {

                            }
                        } else {
                            showLinkActionsDialog = true
                        }
                    },
                    onLongClick = {
                        val bundle = bundleOf("link" to link)
                        navController.navigate(
                            R.id.linkFragment,
                            bundle
                        )
                    },
                    showClickCount = uiPreferences.isClickCounterEnabled(),
                    minimalModeEnabled = uiPreferences.isMinimalModeEnabled(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                )
            }
        }

        if (showLinkActionsDialog) {
            lastClickedLink?.let { link ->
                LinkActionsBottomSheet(
                    link = link,
                    navController = navController,
                    onDialogDismiss = { showLinkActionsDialog = false })
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
