package com.amrdeveloper.linkhub.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.ui.components.FloatingQuickActionsButton
import com.amrdeveloper.linkhub.ui.components.FolderItem
import com.amrdeveloper.linkhub.ui.components.LinkActionsBottomSheet
import com.amrdeveloper.linkhub.ui.components.LinkList
import com.amrdeveloper.linkhub.ui.components.LinkhubToolbar
import com.amrdeveloper.linkhub.ui.components.PagerIndicator
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.openLinkIntent

private const val NUMBER_OF_FOLDERS_PER_PAGE = 6

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    uiPreferences: UiPreferences,
    navController: NavController
) {
    val context = LocalContext.current

    val folders = viewModel.mostUsedLimitedFoldersState.collectAsLazyPagingItems()
    val totalPages =
        (folders.itemCount + NUMBER_OF_FOLDERS_PER_PAGE - 1) / NUMBER_OF_FOLDERS_PER_PAGE
    val foldersPagerState = rememberPagerState(pageCount = { totalPages })

    val links = viewModel.sortedLinksState.collectAsStateWithLifecycle()

    var lastClickedLink by remember { mutableStateOf<Link?>(value = null) }
    var showLinkActionsDialog by remember { mutableStateOf(value = false) }

    Scaffold(
        topBar = { LinkhubToolbar(viewModel(), uiPreferences, navController) },
        floatingActionButton = {
            if (uiPreferences.isQuickActionButtonEnabled()) {
                FloatingQuickActionsButton(navController)
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (folders.itemCount != 0) {
                    Text(
                        text = "Folders",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        color = colorResource(R.color.light_blue_600)
                    )

                    HorizontalPager(
                        state = foldersPagerState,
                        key = folders.itemKey { it.id },
                        verticalAlignment = Alignment.Top,
                        beyondViewportPageCount = 1,
                        modifier = Modifier.fillMaxWidth(),
                        pageContent = { pageIndex ->
                            LazyVerticalGrid(columns = GridCells.Fixed(count = 2)) {
                                for (elementIndexInUI in 0 until NUMBER_OF_FOLDERS_PER_PAGE) {
                                    val itemIndex =
                                        (pageIndex * NUMBER_OF_FOLDERS_PER_PAGE) + elementIndexInUI
                                    if (itemIndex >= folders.itemCount) {
                                        break
                                    }

                                    folders[itemIndex]?.let { folder ->
                                        item {
                                            FolderItem(
                                                folder = folder,
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
                                                minimalModeEnabled = uiPreferences.isMinimalModeEnabled(),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )

                    PagerIndicator(pagerState = foldersPagerState)
                }

                if (links.value.data.isNotEmpty()) {
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
                            if (uiPreferences.isOpenLinkByClickOptionEnabled()) {
                                try {
                                    openLinkIntent(context = context, link = link.url)
                                } catch (_: Exception) {

                                }
                            } else {
                                showLinkActionsDialog = true
                            }
                        },
                        onLongClick = { link ->
                            val bundle = bundleOf("link" to link)
                            navController.navigate(R.id.linkFragment, bundle)
                        },
                        showClickCount = uiPreferences.isClickCounterEnabled(),
                        minimalModeEnabled = uiPreferences.isMinimalModeEnabled()
                    )
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
    )
}