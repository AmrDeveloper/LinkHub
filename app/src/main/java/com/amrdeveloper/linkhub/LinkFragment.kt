package com.amrdeveloper.linkhub

import android.os.Bundle
import android.view.*
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.FragmentLinkBinding
import com.amrdeveloper.linkhub.util.showError
import com.amrdeveloper.linkhub.util.showSnackBar

class LinkFragment : Fragment() {

    private var _binding: FragmentLinkBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentLink: Link
    private var linkFolderID : Int = -1
    private lateinit var folderMenuAdapter: FolderArrayAdapter

    private val linkViewModel: LinkViewModel by viewModels {
        val application = (activity?.application as LinkApplication)
        LinkViewModelFactory(application.folderRepository, application.linkRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val value = arguments?.get("link")
        if(value != null) {
            currentLink = value as Link
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkBinding.inflate(inflater, container, false)

        handleIntentSharedLink()
        handleLinkArgument()
        setupObservers()
        setupFolderListMenu()

        linkViewModel.getFolderList()

        return binding.root
    }

    private fun handleIntentSharedLink() {
        val sharedLink = arguments?.get("shared_link")
        if(sharedLink != null) {
            binding.linkUrlEdit.setText(sharedLink.toString())
        }
    }

    private fun handleLinkArgument() {
        if(::currentLink.isInitialized) {
            binding.linkTitleEdit.setText(currentLink.title)
            binding.linkSubtitleEdit.setText(currentLink.subtitle)
            binding.linkUrlEdit.setText(currentLink.url)
            binding.linkPinnedSwitch.isChecked = currentLink.isPinned
            if(currentLink.folderId != -1) linkViewModel.getFolderWithId(currentLink.folderId)
        }
    }

    private fun setupObservers() {
        linkViewModel.currentFolderLiveData.observe(viewLifecycleOwner, {
            binding.folderNameMenu.setText(it.name, false)
            linkFolderID = it.id
        })

        linkViewModel.folderLiveData.observe(viewLifecycleOwner, {
            folderMenuAdapter.addAll(it)
        })

        linkViewModel.completeSuccessTask.observe(viewLifecycleOwner, {
            findNavController().navigateUp()
        })

        linkViewModel.errorMessages.observe(viewLifecycleOwner, { messageId ->
            activity.showSnackBar(messageId)
        })
    }

    private fun setupFolderListMenu() {
        binding.folderNameMenu.setOnItemClickListener { _, _, position, _ ->
            val folder = folderMenuAdapter.getItem(position)
            if (folder != null) linkFolderID = folder.id
        }

        folderMenuAdapter = FolderArrayAdapter(requireContext())
        folderMenuAdapter.add(Folder("None", false, id = -1))
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
                if(::currentLink.isInitialized) updateCurrentLink()
                else createNewLink()
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

    private fun createNewLink() {
        val title = binding.linkTitleEdit.text.toString()
        val subtitle = binding.linkSubtitleEdit.text.toString()
        val url = binding.linkUrlEdit.text.toString()
        val isPinned = binding.linkPinnedSwitch.isChecked

        if(title.isEmpty()) {
            binding.linkTitleLayout.showError(R.string.error_link_title_empty)
            return
        }

        if(subtitle.isEmpty()) {
            binding.linkSubtitleLayout.showError(R.string.error_link_subtitle_empty)
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

        val link = Link(title, subtitle, url, isPinned)
        if(linkFolderID != -1) link.folderId = linkFolderID

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

        if(subtitle.isEmpty()) {
            binding.linkSubtitleLayout.showError(R.string.error_link_subtitle_empty)
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
        if(linkFolderID != -1) currentLink.folderId = linkFolderID

        linkViewModel.updateLink(currentLink)
    }

    private fun deleteCurrentLink() {
        linkViewModel.deleteLink(currentLink)
    }

    override fun onDestroyView() {
        binding.folderNameMenu.setAdapter(null)
        super.onDestroyView()
        _binding = null
    }
}