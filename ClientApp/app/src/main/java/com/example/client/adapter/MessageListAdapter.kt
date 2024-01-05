package com.example.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.client.databinding.ItemReceiveMessageBinding
import com.example.client.databinding.ItemSentMessageBinding
import com.example.client.utli.formatTimestamp
import com.example.sever.model.Message


class MessageListAdapter(private val userId: Int) :
    ListAdapter<Message, RecyclerView.ViewHolder>(DiffCallback()) {
    companion object {
        const val VIEW_TYPE_SEND = 0
        const val VIEW_TYPE_RECEIVE = 1
    }
    inner class SendViewHolder(private val binding: ItemSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.apply {
                textSentMessage.text = message.content
                textSentTimestamp.text = message.timestamp.formatTimestamp()
            }
        }
    }

    inner class ReceiveViewHolder(private val binding: ItemReceiveMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.apply {
                textReceiveMessage.text = message.content
                textReceiveTimestamp.text = message.timestamp.formatTimestamp()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            SendViewHolder(
                ItemSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ReceiveViewHolder(
                ItemReceiveMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item.sendId == userId) {
            (holder as SendViewHolder).bind(item)
        } else {
            (holder as ReceiveViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).sendId == userId) {
            VIEW_TYPE_SEND
        } else {
            VIEW_TYPE_RECEIVE
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ): Boolean {
            return oldItem == newItem
        }
    }
}