package com.amrdeveloper.linkhub

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var linkAdapter: LinkAdapter

    private var isOptionsButtonClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupListeners()
        setupFoldersList()
        setupLinksList()

        return binding.root
    }

    private fun setupListeners() {
        binding.folderNextImg.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_folderListFragment)
        }

        binding.showAddOptions.setOnClickListener { updateActionOptions() }

        binding.addLinkOption.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_linkFragment)
        }

        binding.addFolderOption.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_folderFragment)
        }
    }

    private fun updateActionOptions() {
        val visibility = if (isOptionsButtonClicked) View.INVISIBLE else View.VISIBLE
        binding.addLinkOption.visibility = visibility
        binding.addFolderOption.visibility = visibility
        isOptionsButtonClicked = isOptionsButtonClicked.not()
        binding.addLinkOption.isClickable = isOptionsButtonClicked
        binding.addFolderOption.isClickable = isOptionsButtonClicked
    }

    private fun setupFoldersList() {
        folderAdapter = FolderAdapter()

        binding.folderList.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = folderAdapter
        }

        val list = arrayListOf(
            Folder(1, "Amr"),
            Folder(2, "Hesham"),
            Folder(3, "Work"),
            Folder(4, "Funny"),
            Folder(1, "Amr"),
            Folder(2, "Hesham"),
            Folder(3, "Work"),
            Folder(4, "Funny"),
        )

        folderAdapter.submitList(list)

        folderAdapter.setOnFolderClickListener(object : FolderAdapter.OnFolderClickListener {
            override fun onFolderClick(folder: Folder) {
                val bundle = bundleOf("folder" to folder)
                navController.navigate(R.id.action_homeFragment_to_linkListFragment, bundle)
            }
        })
    }

    private fun setupLinksList(){
        linkAdapter = LinkAdapter()

        binding.linkList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = linkAdapter
        }

        val list = arrayListOf(
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
            Link(1, "Youtube talk 1", "Youtube talk 2", ""),
        )

        linkAdapter.submitList(list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
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