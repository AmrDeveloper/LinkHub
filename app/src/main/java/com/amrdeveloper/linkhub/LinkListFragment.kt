package com.amrdeveloper.linkhub

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentLinkListBinding

private const val TAG = "LinkListFragment"

class LinkListFragment : Fragment() {

    private var _binding: FragmentLinkListBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentFolder: Folder
    private lateinit var linkAdapter: LinkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        currentFolder = arguments?.get("folder") as Folder
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkListBinding.inflate(inflater, container, false)
        setupLinksList()
        return binding.root
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