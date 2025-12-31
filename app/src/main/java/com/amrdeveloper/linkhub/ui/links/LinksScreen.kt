package com.amrdeveloper.linkhub.ui.links

import android.app.Activity
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
import com.amrdeveloper.linkhub.ui.components.LinkList
import com.amrdeveloper.linkhub.util.LinkBottomSheetDialog
import com.amrdeveloper.linkhub.util.UiPreferences

@Composable
fun LinksScreen(
    currentFolder: Folder,
    viewModel: LinkListViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController,
    activity: Activity, // TODO: Remove and replace it with Compose solution
) {
    LaunchedEffect(true) {
        viewModel.updateFolderId(currentFolder.id)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column {
        FolderHeader(currentFolder, uiState.links.size)

        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
            )
            return
        }

        LinkList(
            links = uiState.links, onClick = { link ->
                viewModel.incrementLinkClickCount(link)
                LinkBottomSheetDialog.launch(activity, link)
            }, onLongClick = { link ->
                val bundle = bundleOf("link" to link)
                navController.navigate(
                    R.id.action_linkListFragment_to_linkFragment,
                    bundle
                )
            }, showClickCount = uiPreferences.isClickCounterEnabled()
        )
    }
}

@Composable
private fun FolderHeader(folder: Folder, linkCount: Int) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = folder.folderColor.drawableId),
            contentDescription = "Folder Icon",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = "${folder.name} : ${linkCount}")
    }
}