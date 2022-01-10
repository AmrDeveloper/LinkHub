package com.amrdeveloper.linkhub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.ListItemLinkBinding

class LinkAdapter : ListAdapter<Link, RecyclerView.ViewHolder>(LinkDiffCallback()) {

    private lateinit var onLinkClick : (Link, Int) -> Unit

    private lateinit var onLinkLongClick : (Link) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val link = getItem(position)
        (holder as LinkViewHolder).bind(link)
        if(::onLinkClick.isInitialized) {
            holder.itemView.setOnClickListener {
                onLinkClick(link, position)
                updateClickCounter(position)
            }
        }

        if(::onLinkLongClick.isInitialized) {
            holder.itemView.setOnLongClickListener {
                onLinkLongClick(link)
                true
            }
        }
    }

    private fun updateClickCounter(position: Int) {
        getItem(position).clickedCount++
        notifyItemChanged(position)
    }

    fun setOnLinkClickListener(listener: (Link, Int) -> Unit) {
        onLinkClick = listener
    }

    fun setOnLinkLongClickListener(listener : (Link) -> Unit) {
        onLinkLongClick = listener
    }

    class LinkViewHolder(private val binding: ListItemLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(link: Link) {
            binding.linkTitle.text = link.title
            binding.linkSubtitle.text = link.subtitle
            binding.linkClickCount.text = link.clickedCount.toString()
            binding.linkPinImg.visibility = if (link.isPinned) View.VISIBLE else View.INVISIBLE
        }
    }
}

private class LinkDiffCallback : DiffUtil.ItemCallback<Link>() {

    override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
        return oldItem == newItem
    }
}