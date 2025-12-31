package com.amrdeveloper.linkhub.ui.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.R
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
                FoldersScreen(
                    viewModel = folderListViewModel,
                    navController = findNavController()
                )
            }
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