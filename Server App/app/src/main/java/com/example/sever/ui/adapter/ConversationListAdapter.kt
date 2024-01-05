package com.example.sever.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sever.databinding.ItemConversationBinding
import com.example.sever.model.ConversationItem
import com.example.sever.ui.utli.formatTimestamp


class ConversationListAdapter(
    private val onLongClickHolder: (conversationId: Int) -> Boolean,
    private val onClickHolder: (conversationId: Int, toUser: String, toUserId: Int) -> Unit
) :
    ListAdapter<ConversationItem, ConversationListAdapter.ItemViewHolder>(DiffCallback()) {
    companion object {
        const val EMPTY_STRING = ""
    }

    inner class ItemViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ConversationItem) {
            binding.apply {
                textUserName.text = item.name
                textLastMessage.text = item.lastMessage ?: EMPTY_STRING
                textTimestamp.text = item.timestamp?.formatTimestamp() ?: EMPTY_STRING
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.apply {
            itemView.setOnClickListener {
                onClickHolder(
                    item.conversationId,
                    item.name,
                    item.userId
                )
            }
            itemView.setOnLongClickListener { onLongClickHolder(item.conversationId) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ConversationItem>() {
        override fun areItemsTheSame(
            oldItem: ConversationItem,
            newItem: ConversationItem
        ): Boolean {
            return oldItem.conversationId == newItem.conversationId
        }

        override fun areContentsTheSame(
            oldItem: ConversationItem,
            newItem: ConversationItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}