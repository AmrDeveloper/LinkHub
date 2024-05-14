package com.amrdeveloper.linkhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.ListItemFolderBinding

class FolderArrayAdapter(
    context: Context,
) : ArrayAdapter<Folder>(context, R.layout.list_item_folder) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_folder, parent, false)
        val binding = ListItemFolderBinding.bind(view)
        val folder = getItem(position)
        if (folder != null) {
            binding.folderNameTxt.text = folder.name
            binding.folderPinImg.visibility = if (folder.isPinned) View.VISIBLE else View.INVISIBLE
            binding.folderIconImg.setImageResource(folder.folderColor.drawableId)
        }
        return view
    }
}