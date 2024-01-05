package com.example.sever.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sever.databinding.FragmentHomeBinding
import com.example.sever.ui.adapter.ConversationListAdapter
import com.example.sever.ui.utli.ServiceBroadcast
import com.example.sever.ui.utli.safeNavigate
import com.example.sever.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        const val DELETE_FRIEND_DIALOG_TITLE = "Delete friend"
        const val DELETE_FRIEND_MESSAGE = "Are you sure you want to delete this friend?"
        const val DELETE_FRIEND_TEXT_POSITIVE = "OK"
        const val DELETE_FRIEND_TEXT_NEGATIVE = "Cancel"
        const val DEFAULT_SCROLL_POSITION = 0
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ConversationListAdapter

    private val viewModel: HomeViewModel by viewModels()
    private val args: HomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ConversationListAdapter(
            onLongClickHolder,
            onClickHolder
        )
        val layoutManager = LinearLayoutManager(requireContext())
        binding.apply {
            recycleConversations.adapter = adapter
            recycleConversations.layoutManager = layoutManager
            textUser.text = args.name
        }
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getAllConversationByUserId(args.userId).collect {
                withContext(Dispatchers.Main) {
                    adapter.submitList(it)
                }
            }
        }
        binding.recycleConversations.smoothScrollToPosition(DEFAULT_SCROLL_POSITION)
    }

    private val onLongClickHolder: (conversationId: Int) -> Boolean = {
        AlertDialog.Builder(requireContext())
            .setTitle(DELETE_FRIEND_DIALOG_TITLE)
            .setMessage(DELETE_FRIEND_MESSAGE)
            .setPositiveButton(DELETE_FRIEND_TEXT_POSITIVE) { _, _ ->
                viewModel.deleteChat(it)
                requireContext().sendBroadcast(Intent(ServiceBroadcast.ACTION_UPDATE_CONVERSATION))
            }.setNegativeButton(DELETE_FRIEND_TEXT_NEGATIVE) { _, _ -> }.create().show()
        true
    }

    private val onClickHolder: (conversationId: Int, toUser: String, toUserId: Int) -> Unit =
        { conversationId, toUser, toUserId ->
            val action = HomeFragmentDirections.actionHomeFragmentToChatFragment(
                conversationId,
                toUser,
                toUserId,
                args.userId
            )
            findNavController().safeNavigate(action)
        }
}