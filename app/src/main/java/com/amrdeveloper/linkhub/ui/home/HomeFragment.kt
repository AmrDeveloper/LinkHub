package com.amrdeveloper.linkhub.ui.home

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var uiPreferences: UiPreferences

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
                HomeScreen(
                    viewModel = viewModel(),
                    navController = findNavController(),
                    uiPreferences = uiPreferences,
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
                    menuInflater.inflate(R.menu.menu_setting, menu)

                    val menuItem = menu.findItem(R.id.search_action)
                    val searchView = menuItem?.actionView as SearchView
                    searchView.queryHint = "Search keyword"
                    searchView.setIconifiedByDefault(true)
                    searchView.setOnQueryTextListener(searchViewQueryListener)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.setting_action -> {
                            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
                            true
                        }

                        else -> false
                    }
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

        override fun onQueryTextChange(query: String?): Boolean {
            // TODO: Update the search, can use the same view model
            // if (query.isNullOrEmpty()) homeViewModel.getSortedLinks()
            // else homeViewModel.getSortedLinksByKeyword(query)
            return false
        }
    }
}