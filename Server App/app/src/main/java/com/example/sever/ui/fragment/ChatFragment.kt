package com.example.sever.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sever.databinding.FragmentChatBinding
import com.example.sever.ui.adapter.MessageListAdapter
import com.example.sever.ui.utli.ServiceBroadcast
import com.example.sever.ui.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageListAdapter

    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()

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
        return binding.root
    }

    private fun getMessage() = lifecycleScope.launch(Dispatchers.IO) {
        viewModel.getMessageByConversation(args.conversationId).collect {
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
                viewModel.sendMessage(
                    textInputMessage.text.toString(),
                    args.conversationId,
                    args.toUserId,
                    args.userId
                )
                textInputMessage.text.clear()
                requireContext().sendBroadcast(Intent(ServiceBroadcast.ACTION_UPDATE_CHAT))
                requireContext().sendBroadcast(Intent(ServiceBroadcast.ACTION_UPDATE_CONVERSATION))
            }
            frameDelete.setOnClickListener {
                viewModel.deleteChat(args.conversationId)
                requireContext().sendBroadcast(Intent(ServiceBroadcast.ACTION_UPDATE_CHAT))
                requireContext().sendBroadcast(Intent(ServiceBroadcast.ACTION_UPDATE_CONVERSATION))
                findNavController().popBackStack()
            }

        }
    }
}