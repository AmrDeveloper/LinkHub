package com.amrdeveloper.linkhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.databinding.ListItemLinkBinding

class LinkAdapter : ListAdapter<Link, RecyclerView.ViewHolder>(LinkDiffCallback()) {

    interface OnLinkClickListener {
        fun onLinkClick(link: Link)
    }

    private lateinit var onLinkClick : OnLinkClickListener

    interface OnLinkLongClickListener {
        fun onLinkLongClick(link: Link)
    }

    private lateinit var onLinkLongClick : OnLinkLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val link = getItem(position)
        (holder as LinkViewHolder).bind(link)
        if(::onLinkClick.isInitialized) {
            holder.itemView.setOnClickListener {
                onLinkClick.onLinkClick(link)
            }
        }

        if(::onLinkLongClick.isInitialized) {
            holder.itemView.setOnLongClickListener {
                onLinkLongClick.onLinkLongClick(link)
                true
            }
        }
    }

    fun setOnLinkClickListener(listener: OnLinkClickListener) {
        onLinkClick = listener
    }

    fun setOnLinkLongClickListener(listener : OnLinkLongClickListener) {
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
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
        return oldItem == newItem
    }
}