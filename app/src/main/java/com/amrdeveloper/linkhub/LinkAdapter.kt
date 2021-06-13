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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val link = getItem(position)
        (holder as LinkViewHolder).bind(link)
    }

    class LinkViewHolder(private val binding: ListItemLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(link: Link) {
            binding.linkTitle.text = link.title
            binding.linkSubtitle.text = link.subtitle
            binding.linkPinImg.visibility = if (link.isPinned) View.VISIBLE else View.GONE
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