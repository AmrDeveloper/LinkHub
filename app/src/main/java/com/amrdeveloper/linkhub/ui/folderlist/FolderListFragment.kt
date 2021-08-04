package com.amrdeveloper.linkhub.ui.folderlist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.FolderAdapter
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.FragmentFolderListBinding
import com.amrdeveloper.linkhub.util.hide
import com.amrdeveloper.linkhub.util.show
import com.amrdeveloper.linkhub.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderListFragment : Fragment() {

    private var _binding : FragmentFolderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var folderAdapter: FolderAdapter
    private val folderListViewModel by viewModels<FolderListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)

        setupFolderRecyclerView()
        setupObservers()

        folderListViewModel.getSortedFolderList()

        return binding.root
    }

    private fun setupObservers() {
        folderListViewModel.foldersLiveData.observe(viewLifecycleOwner, {
            binding.foldersCountTxt.text = getString(R.string.folder_count, it.size)
            setupFoldersListState(it)
        })

        folderListViewModel.dataLoading.observe(viewLifecycleOwner, {
            binding.loadingIndicator.visibility = if(it) View.VISIBLE else View.GONE
        })

        folderListViewModel.errorMessages.observe(viewLifecycleOwner, { messageId ->
            activity.showSnackBar(messageId)
        })
    }

    private fun setupFoldersListState(folders : List<Folder>) {
        if(folders.isEmpty()) {
            binding.folderEmptyIcon.show()
            binding.folderEmptyText.show()
            binding.folderList.hide()
        } else {
            binding.folderEmptyIcon.hide()
            binding.folderEmptyText.hide()
            binding.folderList.show()
            folderAdapter.submitList(folders)
        }
    }

    private fun setupFolderRecyclerView() {
        folderAdapter = FolderAdapter()

        binding.folderList.adapter = folderAdapter
        binding.folderList.layoutManager = LinearLayoutManager(activity)

        folderAdapter.setOnFolderClickListener(object : FolderAdapter.OnFolderClickListener {
            override fun onFolderClick(folder: Folder) {
                folderListViewModel.updateFolderClickCount(folder.id, folder.clickedCount.plus(1))
                val bundle = bundleOf("folder" to folder)
                findNavController().navigate(R.id.action_folderListFragment_to_linkListFragment, bundle)
            }
        })

        folderAdapter.setOnFolderLongClickListener(object :
            FolderAdapter.OnFolderLongClickListener {
            override fun onFolderLongClick(folder: Folder) {
                val bundle = bundleOf("folder" to folder)
                findNavController().navigate(R.id.action_folderListFragment_to_folderFragment, bundle)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)

        val menuItem = menu.findItem(R.id.search_action)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Search keyword"
        searchView.setIconifiedByDefault(true)
        searchView.setOnQueryTextListener(searchViewQueryListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private val searchViewQueryListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(keyword: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(keyword: String?): Boolean {
            if(keyword.isNullOrEmpty()) folderListViewModel.getSortedFolderList()
            else folderListViewModel.getSortedFolderListByKeyword(keyword.trim())
            return false
        }
    }

    override fun onDestroyView() {
        binding.folderList.adapter = null
        super.onDestroyView()
        _binding = null
    }
}