package com.amrdeveloper.linkhub.ui.links

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
import androidx.navigation.fragment.navArgs
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.ui.theme.LinkhubAppTheme
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LinkListFragment : Fragment() {

    @Inject
    lateinit var uiPreferences: UiPreferences

    private val linkListViewModel by viewModels<LinkListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupScreenMenu()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            val safeArguments by navArgs<LinkListFragmentArgs>()
            setContent {
                LinkhubAppTheme(isSystemInDarkTheme = uiPreferences.getThemeType() == Theme.DARK) {
                    LinksScreen(
                        currentFolder = safeArguments.folder,
                        viewModel = linkListViewModel,
                        uiPreferences = uiPreferences,
                        navController = findNavController(),
                    )
                }
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
        override fun onQueryTextSubmit(currencyName: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(keyword: String?): Boolean {
            linkListViewModel.updateSearchQuery(query = keyword?.trim() ?: "")
            return false
        }
    }
}