package com.amrdeveloper.linkhub.ui.linklist

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentLinkListBinding
import com.amrdeveloper.linkhub.ui.adapter.LinkAdapter
import com.amrdeveloper.linkhub.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LinkListFragment : Fragment() {

    private var _binding: FragmentLinkListBinding? = null
    private val binding get() = _binding!!

    private val safeArguments by navArgs<LinkListFragmentArgs>()

    @Inject lateinit var uiPreferences: UiPreferences

    private lateinit var currentFolder: Folder
    private lateinit var linkAdapter: LinkAdapter
    private val linkListViewModel by viewModels<LinkListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        currentFolder = safeArguments.folder
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
        linkListViewModel.linksLiveData.observe(viewLifecycleOwner) {
            setupFolderHeaderInfo(it.size)
            setupLinksListState(it)
        }

        linkListViewModel.dataLoading.observe(viewLifecycleOwner) {
            binding.loadingIndicator.visibility = if (it) View.VISIBLE else View.GONE
        }

        linkListViewModel.errorMessages.observe(viewLifecycleOwner) { messageId ->
            activity.showSnackBar(messageId)
        }
    }

    private fun setupLinksListState(links : List<Link>) {
        if(links.isEmpty()) {
            binding.linkEmptyLottie.show()
            binding.linkEmptyLottie.resumeAnimation()
            binding.linkEmptyText.show()
            binding.linkList.hide()
        } else {
            binding.linkEmptyLottie.hide()
            binding.linkEmptyLottie.pauseAnimation()
            binding.linkEmptyText.hide()
            binding.linkList.show()
            linkAdapter.submitList(links)
        }
    }

    private fun setupLinksList(){
        linkAdapter = LinkAdapter()
        linkAdapter.setEnableClickCounter(uiPreferences.isClickCounterEnabled())

        binding.linkList.layoutManager = LinearLayoutManager(context)
        binding.linkList.adapter = linkAdapter

        linkAdapter.setOnLinkClickListener { link, _ ->
            linkListViewModel.updateLinkClickCount(link.id, link.clickedCount + 1)
            LinkBottomSheetDialog.launch(requireActivity(), link)
        }

        linkAdapter.setOnLinkLongClickListener {
            val bundle = bundleOf("link" to it)
            findNavController().navigate(R.id.action_linkListFragment_to_linkFragment, bundle)
        }
    }

    private fun setupFolderHeaderInfo(size : Int) {
        binding.folderInfoHeaderTxt.text = "${currentFolder.name}: ${size}"
        binding.folderInfoHeaderTxt.setCompoundDrawablesWithIntrinsicBounds(currentFolder.folderColor.drawableId, 0, 0, 0)
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