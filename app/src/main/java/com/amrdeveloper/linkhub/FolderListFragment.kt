package com.amrdeveloper.linkhub

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.FragmentFolderListBinding

class FolderListFragment : Fragment() {

    private var _binding : FragmentFolderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var folderAdapter: FolderAdapter
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFolderRecyclerView()
        navController = Navigation.findNavController(view)
    }

    private fun setupFolderRecyclerView() {
        folderAdapter = FolderAdapter()

        binding.folderList.apply {
            setHasFixedSize(true)
            adapter = folderAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        folderAdapter.setOnFolderClickListener(object : FolderAdapter.OnFolderClickListener {
            override fun onFolderClick(folder: Folder) {
                val bundle = bundleOf("folder" to folder)
                navController.navigate(R.id.action_folderListFragment_to_linkListFragment, bundle)
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

        super.onCreateOptionsMenu(menu, inflater);
    }

    private val searchViewQueryListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(currencyName: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}