package com.amrdeveloper.linkhub.ui.folderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.ui.composables.FolderList
import com.amrdeveloper.linkhub.ui.composables.FolderViewKind
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderListFragment : Fragment() {

    private val folderListViewModel by viewModels<FolderListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupScreenMenu()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                FoldersScreen()
            }
        }
    }

    @Composable
    fun FoldersScreen(viewModel: FolderListViewModel = viewModel()) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        Column {
            Text(text = "Folders: ${uiState.folders.size}", modifier = Modifier.padding(16.dp))

            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                return
            }

            FolderList(
                folders = uiState.folders,
                viewKind = FolderViewKind.List,
                onClick = { folder ->
                    folderListViewModel.incrementFolderClickCount(folder)

                    val bundle = bundleOf("folder" to folder)
                    findNavController().navigate(
                        R.id.action_folderListFragment_to_linkListFragment,
                        bundle
                    )
                },
                onLongClick = { folder ->
                    val bundle = bundleOf("folder" to folder)
                    findNavController().navigate(
                        R.id.action_folderListFragment_to_folderFragment,
                        bundle
                    )
                }
            )
        }
    }

    private fun setupScreenMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_home, menu)

                    val menuItem = menu.findItem(R.id.search_action)
                    val searchView = menuItem?.actionView as SearchView
                    searchView.queryHint = "Search keyword"
                    searchView.setIconifiedByDefault(true)
                    searchView.setOnQueryTextListener(searchViewQueryListener)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return false
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private val searchViewQueryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(keyword: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(keyword: String?): Boolean {
            folderListViewModel.updateSearchQuery(keyword?.trim() ?: "")
            return false
        }
    }
}