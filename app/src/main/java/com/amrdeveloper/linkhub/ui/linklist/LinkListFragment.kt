package com.amrdeveloper.linkhub.ui.linklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
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
import androidx.navigation.fragment.navArgs
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.ui.composables.LinkList
import com.amrdeveloper.linkhub.util.LinkBottomSheetDialog
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LinkListFragment : Fragment() {

    private val safeArguments by navArgs<LinkListFragmentArgs>()

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

            setContent {
                LinksScreen(safeArguments.folder, linkListViewModel)
            }
        }
    }

    @Composable
    fun LinksScreen(currentFolder : Folder, viewModel: LinkListViewModel = viewModel()) {
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
                    linkListViewModel.incrementLinkClickCount(link)
                    LinkBottomSheetDialog.launch(requireActivity(), link)
                }, onLongClick = { link ->
                    val bundle = bundleOf("link" to link)
                    findNavController().navigate(
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