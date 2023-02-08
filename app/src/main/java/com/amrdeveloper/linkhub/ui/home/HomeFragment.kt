package com.amrdeveloper.linkhub.ui.home

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentHomeBinding
import com.amrdeveloper.linkhub.ui.adapter.FolderAdapter
import com.amrdeveloper.linkhub.ui.adapter.ItemSwipeCallback
import com.amrdeveloper.linkhub.ui.adapter.LinkAdapter
import com.amrdeveloper.linkhub.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var uiPreferences: UiPreferences

    private lateinit var folderAdapter: FolderAdapter
    private lateinit var linkAdapter: LinkAdapter
    private val homeViewModel by viewModels<HomeViewModel>()

    private val rotateOpen by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open) }
    private val rotateClose by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close) }

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

        setupFoldersList()
        setupLinksList()
        setupListeners()
        setupObservers()

        homeViewModel.getTopLimitedFolders(6)
        homeViewModel.getSortedLinks()

        return binding.root
    }

    private fun setupObservers() {
        homeViewModel.folderLiveData.observe(viewLifecycleOwner) {
            setupFoldersListState(it)
        }

        homeViewModel.linkLiveData.observe(viewLifecycleOwner) {
            setupLinksListState(it)
            binding.linksCount.text = it.size.toString()
        }

        homeViewModel.errorMessages.observe(viewLifecycleOwner) { messageId ->
            activity.showSnackBar(messageId)
        }
    }

    private fun setupListeners() {
        binding.folderNextImg.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_folderListFragment)
        }

        binding.showAddOptions.setOnClickListener { updateActionOptions() }

        binding.addLinkOption.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_linkFragment)
        }

        binding.addFolderOption.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_folderFragment)
        }
    }

    private fun updateActionOptions() {
        val visibility = if (isOptionsButtonClicked) View.INVISIBLE else View.VISIBLE
        val animation = if(isOptionsButtonClicked) rotateClose else rotateOpen
        isOptionsButtonClicked = isOptionsButtonClicked.not()

        binding.addLinkOption.visibility = visibility
        binding.addLinkOption.isClickable = isOptionsButtonClicked

        binding.addFolderOption.visibility = visibility
        binding.addFolderOption.isClickable = isOptionsButtonClicked

        binding.showAddOptions.startAnimation(animation)
    }

    private fun setupFoldersList() {
        folderAdapter = FolderAdapter()

        binding.folderList.layoutManager = GridLayoutManager(context, 2)
        binding.folderList.adapter = folderAdapter

        folderAdapter.setOnFolderClickListener {
            homeViewModel.updateFolderClickCount(it.id, it.clickedCount.plus(1))
            val bundle = bundleOf("folder" to it)
            findNavController().navigate(R.id.action_homeFragment_to_linkListFragment, bundle)
        }

        folderAdapter.setOnFolderLongClickListener{
            val bundle = bundleOf("folder" to it)
            findNavController().navigate(R.id.action_homeFragment_to_folderFragment, bundle)
        }
    }

    private fun setupLinksList() {
        linkAdapter = LinkAdapter()
        linkAdapter.setEnableClickCounter(uiPreferences.getEnableClickCounter())

        binding.linkList.layoutManager = LinearLayoutManager(context)
        binding.linkList.adapter = linkAdapter

        val context = requireContext()
        val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
        val background = ColorDrawable(ContextCompat.getColor(context, R.color.red))

        val swipeHandler = ItemSwipeCallback(deleteIcon, background) { holder ->
            val position = holder.adapterPosition
            val link = linkAdapter.currentList[position]
            val list = linkAdapter.currentList.toMutableList()
            list.removeAt(position)
            linkAdapter.submitList(list)
            homeViewModel.deleteLink(link)

            activity.showSnackBar(R.string.message_link_deleted, R.string.undo) {
                list.add(position, link)
                linkAdapter.submitList(list)
                linkAdapter.notifyItemInserted(position)
                homeViewModel.insertLink(link)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.linkList)

        linkAdapter.setOnLinkClickListener { link, _ ->
            homeViewModel.updateLinkClickCount(link.id, link.clickedCount + 1)
            LinkBottomSheetDialog.launch(requireActivity(), link)
        }

        linkAdapter.setOnLinkLongClickListener {
            val bundle = bundleOf("link" to it)
            findNavController().navigate(R.id.action_homeFragment_to_linkFragment, bundle)
        }
    }

    private fun setupFoldersListState(folders: List<Folder>) {
        if (folders.isEmpty()) {
            binding.folderHeaderTxt.hide()
            binding.folderNextImg.hide()
            binding.folderList.hide()
        } else {
            binding.folderHeaderTxt.show()
            binding.folderNextImg.show()
            binding.folderList.show()
            folderAdapter.submitList(folders)
        }
    }

    private fun setupLinksListState(links: List<Link>) {
        if (links.isEmpty()) {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        inflater.inflate(R.menu.menu_setting, menu)

        val menuItem = menu.findItem(R.id.search_action)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Search keyword"
        searchView.setIconifiedByDefault(true)
        searchView.setOnQueryTextListener(searchViewQueryListener)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_action -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val searchViewQueryListener = object : SearchView.OnQueryTextListener {

        override fun onQueryTextSubmit(keyword: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(query: String?): Boolean {
            if (query.isNullOrEmpty()) homeViewModel.getSortedLinks()
            else homeViewModel.getSortedLinksByKeyword(query)
            return false
        }
    }

    override fun onDestroyView() {
        binding.folderList.adapter = null
        binding.linkList.adapter = null
        super.onDestroyView()
        _binding = null
    }
}