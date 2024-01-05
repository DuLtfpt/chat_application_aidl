package com.example.client.fragment

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.databinding.FragmentHomeBinding
import com.example.client.adapter.ConversationListAdapter
import com.example.client.broadcast.ClientBroadcastReceiver
import com.example.client.utli.safeNavigate
import com.example.sever.model.ConversationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ConversationListAdapter
    private lateinit var homeFragmentCallBack: HomeFragmentCallBack
    private val args: HomeFragmentArgs by navArgs()

    companion object {
        const val DELETE_FRIEND_DIALOG_TITLE = "Delete friend"
        const val DELETE_FRIEND_MESSAGE = "Are you sure you want to delete this friend?"
        const val DELETE_FRIEND_TEXT_POSITIVE = "OK"
        const val DELETE_FRIEND_TEXT_NEGATIVE = "Cancel"
        const val DEFAULT_SCROLL_POSITION = 0
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ClientBroadcastReceiver.ACTION_HOME_FRAGMENT) getConversation()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        getConversation()
        val intentFilter = IntentFilter(ClientBroadcastReceiver.ACTION_HOME_FRAGMENT)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, intentFilter)
        return binding.root
    }

    interface HomeFragmentCallBack {
        fun deleteChatFromHome(conversationId: Int)
        fun getAllConversationByUserId(userId: Int): Flow<List<ConversationItem>>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the activity implements the callback interface
        if (context is HomeFragmentCallBack) {
            homeFragmentCallBack = context
        } else {
            throw RuntimeException("$context must implement HomeFragmentCallBack")
        }
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
        binding.recycleConversations.smoothScrollToPosition(DEFAULT_SCROLL_POSITION)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    private fun getConversation() {
        lifecycleScope.launch(Dispatchers.IO) {
            homeFragmentCallBack.getAllConversationByUserId(args.userId).collect {
                withContext(Dispatchers.Main) {
                    adapter.submitList(it.toMutableList())
                }
            }
        }
    }

    private val onLongClickHolder: (conversationId: Int) -> Boolean = {
        AlertDialog.Builder(requireContext())
            .setTitle(DELETE_FRIEND_DIALOG_TITLE)
            .setMessage(DELETE_FRIEND_MESSAGE)
            .setPositiveButton(DELETE_FRIEND_TEXT_POSITIVE) { _, _ ->
                homeFragmentCallBack.deleteChatFromHome(it)
                getConversation()
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