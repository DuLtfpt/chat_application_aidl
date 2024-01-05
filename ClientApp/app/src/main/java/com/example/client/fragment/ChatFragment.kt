package com.example.client.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.client.adapter.MessageListAdapter
import com.example.client.broadcast.ClientBroadcastReceiver
import com.example.client.databinding.FragmentChatBinding
import com.example.sever.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var adapter: MessageListAdapter
    private lateinit var chatFragmentCallBack: ChatFragmentCallBack
    private lateinit var recyclerView: RecyclerView

    private val args: ChatFragmentArgs by navArgs()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ClientBroadcastReceiver.ACTION_CHAT_FRAGMENT)
                getMessage()
        }
    }

    interface ChatFragmentCallBack {
        fun getMessageByConversation(conversationId: Int): Flow<List<Message>>
        fun sendMessage(input: String, conversationId: Int, receiveId: Int, sendId: Int)
        fun deleteChatFromChat(conversationId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the activity implements the callback interface
        if (context is ChatFragmentCallBack) {
            chatFragmentCallBack = context
        } else {
            throw RuntimeException("$context must implement ChatFragmentCallBack")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        adapter = MessageListAdapter(args.userId)
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.recyclerChat
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        getMessage()
        val intentFilter = IntentFilter(ClientBroadcastReceiver.ACTION_CHAT_FRAGMENT)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, intentFilter)
        return binding.root
    }

    private fun getMessage() = lifecycleScope.launch(Dispatchers.IO) {
        chatFragmentCallBack.getMessageByConversation(args.conversationId).collect {
            withContext(Dispatchers.Main) {
                adapter.submitList(it)
                val position = it.size - 1
                if (position >= 0) {
                    recyclerView.smoothScrollToPosition(position)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            textToUser.text = args.toUser
            frameBack.setOnClickListener {
                findNavController().popBackStack()
            }
            frameSend.setOnClickListener {
                chatFragmentCallBack.sendMessage(
                    textInputMessage.text.toString(),
                    args.conversationId,
                    args.toUserId,
                    args.userId
                )
                textInputMessage.text.clear()
                getMessage()
            }
            frameDelete.setOnClickListener {
                chatFragmentCallBack.deleteChatFromChat(args.conversationId)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}