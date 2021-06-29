package com.amrdeveloper.linkhub

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentLinkListBinding
import com.amrdeveloper.linkhub.util.LinkBottomSheetDialog
import com.amrdeveloper.linkhub.util.hide
import com.amrdeveloper.linkhub.util.show
import com.amrdeveloper.linkhub.util.showSnackBar

class LinkListFragment : Fragment() {

    private var _binding: FragmentLinkListBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentFolder: Folder
    private lateinit var linkAdapter: LinkAdapter

    private val linkListViewModel: LinkListViewModel by viewModels {
        val application = (activity?.application as LinkApplication)
        LinkListViewModelFactory(application.linkRepository)
    }

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
        setupObservers()

        linkListViewModel.getFolderLinkList(currentFolder.id)

        return binding.root
    }

    private fun setupObservers() {
        linkListViewModel.linksLiveData.observe(viewLifecycleOwner, {
            binding.linksCountTxt.text = getString(R.string.links_count, it.size)
            setupLinksListState(it)
        })

        linkListViewModel.dataLoading.observe(viewLifecycleOwner, {
            binding.loadingIndicator.visibility = if(it) View.VISIBLE else View.GONE
        })

        linkListViewModel.errorMessages.observe(viewLifecycleOwner, { messageId ->
            activity.showSnackBar(messageId)
        })
    }

    private fun setupLinksListState(links : List<Link>) {
        if(links.isEmpty()) {
            binding.linkEmptyIcon.show()
            binding.linkEmptyText.show()
            binding.linkList.hide()
        } else {
            binding.linkEmptyIcon.hide()
            binding.linkEmptyText.hide()
            binding.linkList.show()
            linkAdapter.submitList(links)
        }
    }

    private fun setupLinksList(){
        linkAdapter = LinkAdapter()

        binding.linkList.layoutManager = LinearLayoutManager(context)
        binding.linkList.adapter = linkAdapter

        linkAdapter.setOnLinkClickListener(object : LinkAdapter.OnLinkClickListener {
            override fun onLinkClick(link: Link, position: Int) {
                linkListViewModel.updateLinkClickCount(link.id, link.clickedCount + 1)
                LinkBottomSheetDialog.launch(requireActivity(), link)
            }
        })

        linkAdapter.setOnLinkLongClickListener(object : LinkAdapter.OnLinkLongClickListener {
            override fun onLinkLongClick(link: Link) {
                val bundle = bundleOf("link" to link)
                findNavController().navigate(R.id.action_linkListFragment_to_linkFragment, bundle)
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

        override fun onQueryTextSubmit(currencyName: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(keyword: String?): Boolean {
            if(keyword.isNullOrEmpty()) linkListViewModel.getFolderLinkList(currentFolder.id)
            else linkListViewModel.getFolderLinkListByKeyword(currentFolder.id, keyword)
            return false
        }
    }

    override fun onDestroyView() {
        binding.linkList.adapter = null
        super.onDestroyView()
        _binding = null
    }
}