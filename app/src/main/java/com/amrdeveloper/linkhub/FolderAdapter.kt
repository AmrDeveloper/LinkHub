package com.amrdeveloper.linkhub

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.linkhub.data.Folder
import com.amrdeveloper.linkhub.databinding.ListItemFolderBinding

class FolderAdapter : ListAdapter<Folder, RecyclerView.ViewHolder>(FolderDiffCallback()){

    interface OnFolderClickListener {
        fun onFolderClick(folder: Folder);
    }

    private lateinit var onFolderClickListener : OnFolderClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val folder = getItem(position)
        (holder as FolderViewHolder).bind(folder)
        if(::onFolderClickListener.isInitialized) {
            holder.itemView.setOnClickListener {
                onFolderClickListener.onFolderClick(folder)
            }
        }
    }

    fun setOnFolderClickListener(listener: OnFolderClickListener) {
        onFolderClickListener = listener
    }

    class FolderViewHolder(private val binding: ListItemFolderBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(folder : Folder) {
            binding.folderNameTxt.text = folder.name
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