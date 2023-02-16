package com.amrdeveloper.linkhub.ui.folder

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.FragmentFolderBinding
import com.amrdeveloper.linkhub.util.showError
import com.amrdeveloper.linkhub.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderFragment : Fragment() {

    private var _binding : FragmentFolderBinding? = null
    private val binding get() = _binding!!

    private val safeArguments by navArgs<FolderFragmentArgs>()

    private lateinit var currentFolder: Folder
    private val folderViewModel by viewModels<FolderViewModel>()

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
                if(::currentFolder.isInitialized) updateFolder()
                else createNewFolder()
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
        folderViewModel.deleteFolder(currentFolder.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}