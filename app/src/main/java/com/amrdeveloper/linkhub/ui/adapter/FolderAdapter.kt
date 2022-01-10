package com.amrdeveloper.linkhub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.ListItemFolderBinding

class FolderAdapter : ListAdapter<Folder, RecyclerView.ViewHolder>(FolderDiffCallback()) {

    private lateinit var onFolderClickListener: (Folder) -> Unit

    private lateinit var onFolderLongClickListener: (Folder) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            ListItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val folder = getItem(position)
        (holder as FolderViewHolder).bind(folder)
        if (::onFolderClickListener.isInitialized) {
            holder.itemView.setOnClickListener {
                onFolderClickListener(folder)
            }
        }
        if (::onFolderLongClickListener.isInitialized) {
            holder.itemView.setOnLongClickListener {
                onFolderLongClickListener(folder)
                true
            }
        }
    }

    fun setOnFolderClickListener(listener: (Folder) -> Unit) {
        onFolderClickListener = listener
    }

    fun setOnFolderLongClickListener(listener: (Folder) -> Unit) {
        onFolderLongClickListener = listener
    }

    class FolderViewHolder(private val binding: ListItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(folder: Folder) {
            binding.folderNameTxt.text = folder.name
            binding.folderPinImg.visibility = if (folder.isPinned) View.VISIBLE else View.GONE
            binding.folderIconImg.setImageResource(folder.folderColor.drawableId)
        }
    }
}

private class FolderDiffCallback : DiffUtil.ItemCallback<Folder>() {

    override fun areItemsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Folder, newItem: Folder): Boolean {
        return oldItem == newItem
    }
}