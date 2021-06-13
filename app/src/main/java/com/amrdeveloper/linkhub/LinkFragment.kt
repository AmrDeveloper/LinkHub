package com.amrdeveloper.linkhub

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import com.amrdeveloper.linkhub.databinding.FragmentLinkBinding

private const val TAG = "LinkFragment"

class LinkFragment : Fragment() {

    private var _binding: FragmentLinkBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLinkBinding.inflate(inflater, container, false)

        val items = listOf("None", "Option 1", "Option 2", "Option 3")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_folder_popup, items)
        binding.folderNameMenu.setAdapter(adapter)
        binding.folderNameMenu.setOnItemClickListener { parent, view, position, id ->
            Log.d(TAG, "onCreateView: ${position}")
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_action -> {
                //TODO: Save or update the link
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNewLink() {
        val title = binding.linkTitleEdit.text.toString()
        val subtitle = binding.linkSubtitleEdit.text.toString()
        val url = binding.linkUrlEdit.text.toString()
        val isPinned = binding.linkPinnedSwitch.isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}