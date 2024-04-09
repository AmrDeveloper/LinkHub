package com.amrdeveloper.linkhub.ui.folder

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.FragmentFolderBinding
import com.amrdeveloper.linkhub.util.CREATED_FOLDER_NAME_KEY
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.showError
import com.amrdeveloper.linkhub.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FolderFragment : Fragment() {

    private var _binding : FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private val safeArguments by navArgs<FolderFragmentArgs>()

    private lateinit var currentFolder: Folder
    private val folderViewModel by viewModels<FolderViewModel>()

    @Inject lateinit var uiPreferences: UiPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        safeArguments.folder?.let { currentFolder = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolderBinding.inflate(inflater, container, false)

        handleFolderArgument()
        setupObservers()

        return binding.root
    }

    private fun handleFolderArgument() {
        if(::currentFolder.isInitialized) {
            binding.folderTitleEdit.setText(currentFolder.name)
            binding.folderPinnedSwitch.isChecked = currentFolder.isPinned
            binding.folderColorSelector.setCurrentFolderColor(currentFolder.folderColor)
        }
    }

    private fun setupObservers() {
        folderViewModel.completeSuccessTask.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        folderViewModel.errorMessages.observe(viewLifecycleOwner) { messageId ->
            activity.showSnackBar(messageId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        inflater.inflate(R.menu.menu_delete, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_action -> {
                createOrUpdateFolder()
                true
            }
            R.id.delete_action -> {
                if(::currentFolder.isInitialized) deleteFolder()
                else findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createOrUpdateFolder() {
        if(::currentFolder.isInitialized) updateFolder()
        else createNewFolder()
    }

    private fun createNewFolder() {
        val name = binding.folderTitleEdit.text.toString().trim()
        if(name.isEmpty()) {
            binding.folderTitleLayout.showError(R.string.error_folder_name_empty)
            return
        }
        if(name.length < 3) {
            binding.folderTitleLayout.showError(R.string.error_folder_name_small)
            return
        }

        val isPinned = binding.folderPinnedSwitch.isChecked
        val folderColor = binding.folderColorSelector.getCurrentFolderColor()
        val folder = Folder(name, isPinned, folderColor = folderColor)

        // Store the created folder name only if previous fragment is LinkFragment
        val previousFragment = findNavController().previousBackStackEntry?.destination?.id
        if (previousFragment == R.id.linkFragment) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                CREATED_FOLDER_NAME_KEY,
                folder.name
            )
        }

        folderViewModel.createNewFolder(folder)
    }

    private fun updateFolder() {
        val name = binding.folderTitleEdit.text.toString().trim()
        if(name.isEmpty()) {
            binding.folderTitleLayout.showError(R.string.error_folder_name_empty)
            return
        }

        if(name.length < 3) {
            binding.folderTitleLayout.showError(R.string.error_folder_name_small)
            return
        }

        currentFolder.name = name
        currentFolder.isPinned = binding.folderPinnedSwitch.isChecked
        currentFolder.folderColor = binding.folderColorSelector.getCurrentFolderColor()

        folderViewModel.updateFolder(currentFolder)
    }

    private fun deleteFolder() {
        if(uiPreferences.isDefaultFolderEnabled() &&
            uiPreferences.getDefaultFolderId() == currentFolder.id)
            uiPreferences.deleteDefaultFolder()
        folderViewModel.deleteFolder(currentFolder.id)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val isAutoSavingEnabled = uiPreferences.isAutoSavingEnabled()
                if (isAutoSavingEnabled) createOrUpdateFolder()
                this.remove()
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}