package com.amrdeveloper.linkhub.ui.link

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentLinkBinding
import com.amrdeveloper.linkhub.ui.adapter.FolderArrayAdapter
import com.amrdeveloper.linkhub.ui.widget.PinnedLinksWidget
import com.amrdeveloper.linkhub.util.CREATED_FOLDER_NAME_KEY
import com.amrdeveloper.linkhub.util.CREATE_FOLDER_ID
import com.amrdeveloper.linkhub.util.FOLDER_NONE_ID
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.showError
import com.amrdeveloper.linkhub.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import javax.inject.Inject

@AndroidEntryPoint
class LinkFragment : Fragment() {

    private var _binding: FragmentLinkBinding? = null
    private val binding get() = _binding!!

    private val safeArguments by navArgs<LinkFragmentArgs>()

    private lateinit var currentLink: Link
    private var linkFolderID : Int = -1
    private lateinit var folderMenuAdapter: FolderArrayAdapter
    private val linkViewModel by viewModels<LinkViewModel>()

    private val dateFormatter = DateFormat.getDateTimeInstance()

    @Inject lateinit var uiPreferences: UiPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        safeArguments.link?.let { currentLink = it }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLinkBinding.inflate(inflater, container, false)
        handleDefaultFolder()
        handleIntentSharedLink()
        handleLinkArgument()
        setupObservers()
        setupFolderListMenu()

        linkViewModel.getFolderList()

        return binding.root
    }

    private fun handleDefaultFolder(){
        if (uiPreferences.isDefaultFolderEnabled()){
            val defFolderId = uiPreferences.getDefaultFolderId()
            if (defFolderId!=-1){
                linkViewModel.getFolderWithId(defFolderId)
            }
        }
    }

    private fun handleIntentSharedLink() {
        val sharedLink = arguments?.getString("shared_link") ?: return

        if(URLUtil.isValidUrl(sharedLink).not()) {
            binding.linkUrlLayout.showError(R.string.error_link_url_invalid)
            return
        }

        binding.linkUrlEdit.setText(sharedLink)
        linkViewModel.generateLinkTitleAndSubTitle(sharedLink)
    }

    private fun handleLinkArgument() {
        if(::currentLink.isInitialized) {
            binding.linkTitleEdit.setText(currentLink.title)
            binding.linkSubtitleEdit.setText(currentLink.subtitle)
            binding.linkUrlEdit.setText(currentLink.url)
            binding.linkPinnedSwitch.isChecked = currentLink.isPinned
            val linkCreatedStamp = if (currentLink.createdTime == 0L) System.currentTimeMillis() else currentLink.createdTime
            val formattedCreationDate = dateFormatter.format(linkCreatedStamp)
            binding.linkCreatedStatus.text = getString(R.string.created_at) + " ${formattedCreationDate}"
            if (currentLink.isUpdated) {
                val linkUpdatedStamp = if (currentLink.createdTime == 0L) System.currentTimeMillis() else currentLink.lastUpdatedTime
                val formattedUpdateDate = dateFormatter.format(linkUpdatedStamp)
                binding.linkUpdatedStatus.text = getString(R.string.updated_at) + " ${formattedUpdateDate}"
            }
            if (currentLink.folderId != -1) linkViewModel.getFolderWithId(currentLink.folderId)
        }
    }

    private fun setActiveFolderToFolderList(folders: Iterable<Folder>, id: Int){
        val folder = folders.find { it.id == currentLink.folderId }
        folder?.let {
            binding.folderNameMenu.setText(it.name, false)
        }
    }

    private fun setupObservers() {
        linkViewModel.currentFolderLiveData.observe(viewLifecycleOwner) {
            binding.folderNameMenu.setText(it.name, false)
            linkFolderID = it.id
        }

        linkViewModel.folderLiveData.observe(viewLifecycleOwner) { folders ->
            folderMenuAdapter.clear()
            folderMenuAdapter.add(Folder(getString(R.string.folder_create), false, id = CREATE_FOLDER_ID))
            folderMenuAdapter.add(Folder(getString(R.string.none), false, id = FOLDER_NONE_ID))
            folderMenuAdapter.addAll(folders)
            binding.folderNameMenu.text.clear()

            // Check if user created a new folder for this link and suggest it as the current link folder
            val newFolderCreatedName = findNavController().currentBackStackEntry?.savedStateHandle?.get<String>(CREATED_FOLDER_NAME_KEY)

            if (newFolderCreatedName != null) {
                val lastCreatedFolder =
                    folders.find { it.name == newFolderCreatedName } ?: return@observe
                binding.folderNameMenu.setText(newFolderCreatedName, false)
                linkFolderID = lastCreatedFolder.id
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<String>(
                    CREATED_FOLDER_NAME_KEY
                )
            } else if (::currentLink.isInitialized ) {
                setActiveFolderToFolderList(folders, currentLink.folderId)
            } else if (uiPreferences.isDefaultFolderEnabled() && uiPreferences.getDefaultFolderId()!=-1){
                setActiveFolderToFolderList(folders, uiPreferences.getDefaultFolderId())
            }
        }

        linkViewModel.linkInfoLiveData.observe(viewLifecycleOwner) {
            binding.linkTitleEdit.setText(it.linkTitle)
            binding.linkSubtitleEdit.setText(it.linkSubtitle)
        }

        linkViewModel.completeSuccessTask.observe(viewLifecycleOwner) {
            PinnedLinksWidget.refresh(requireContext())
            findNavController().navigateUp()
        }

        linkViewModel.errorMessages.observe(viewLifecycleOwner) { messageId ->
            activity.showSnackBar(messageId)
        }
    }

    private fun setupFolderListMenu() {
        binding.folderNameMenu.setOnItemClickListener { _, _, position, _ ->
            val folder = folderMenuAdapter.getItem(position) ?: return@setOnItemClickListener
            linkFolderID = when (folder.id) {
                CREATE_FOLDER_ID -> {
                    binding.folderNameMenu.setSelection(position + 1)
                    findNavController().navigate(R.id.action_linkFragment_to_folderFragment)
                    FOLDER_NONE_ID
                }
                else -> {
                    if(uiPreferences.isDefaultFolderEnabled()) {
                        uiPreferences.setDefaultFolderId(folder.id)
                    }
                    folder.id
                }
            }
        }

        folderMenuAdapter = FolderArrayAdapter(requireContext())
        binding.folderNameMenu.setAdapter(folderMenuAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        inflater.inflate(R.menu.menu_delete, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_action -> {
                createOrUpdateLink()
                true
            }
            R.id.delete_action -> {
                if(::currentLink.isInitialized) deleteCurrentLink()
                else findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createOrUpdateLink() {
        if(::currentLink.isInitialized) updateCurrentLink()
        else createNewLink()
    }

    private fun createNewLink() {
        val title = binding.linkTitleEdit.text.toString()
        val subtitle = binding.linkSubtitleEdit.text.toString()
        val url = binding.linkUrlEdit.text.toString()
        val isPinned = binding.linkPinnedSwitch.isChecked

        if(title.isEmpty()) {
            binding.linkTitleLayout.showError(R.string.error_link_title_empty)
            return
        }

        if(url.isEmpty()) {
            binding.linkUrlLayout.showError(R.string.error_link_url_empty)
            return
        }

        if(URLUtil.isValidUrl(url).not()) {
            binding.linkUrlLayout.showError(R.string.error_link_url_invalid)
            return
        }

        val link = Link(title, subtitle, url, isPinned, folderId = linkFolderID)
        linkViewModel.createNewLink(link)
    }

    private fun updateCurrentLink() {
        val title = binding.linkTitleEdit.text.toString()
        val subtitle = binding.linkSubtitleEdit.text.toString()
        val url = binding.linkUrlEdit.text.toString()
        val isPinned = binding.linkPinnedSwitch.isChecked

        if(title.isEmpty()) {
            binding.linkTitleLayout.showError(R.string.error_link_title_empty)
            return
        }

        if(url.isEmpty()) {
            binding.linkUrlLayout.showError(R.string.error_link_url_empty)
            return
        }

        if(URLUtil.isValidUrl(url).not()) {
            binding.linkUrlLayout.showError(R.string.error_link_url_invalid)
            return
        }

        currentLink.title = title
        currentLink.subtitle = subtitle
        currentLink.url = url
        currentLink.isPinned = isPinned
        currentLink.isUpdated = true
        currentLink.folderId = linkFolderID
        currentLink.lastUpdatedTime = System.currentTimeMillis()

        linkViewModel.updateLink(currentLink)
    }

    private fun deleteCurrentLink() {
        linkViewModel.deleteLink(currentLink)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val isAutoSavingEnabled = uiPreferences.isAutoSavingEnabled()
                if (isAutoSavingEnabled) createOrUpdateLink()
                this.remove()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        binding.folderNameMenu.setAdapter(null)
        super.onDestroyView()
        _binding = null
    }
}