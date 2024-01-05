package com.example.client

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.client.broadcast.ClientBroadcastReceiver
import com.example.client.databinding.ActivityMainBinding
import com.example.client.fragment.ChatFragment
import com.example.client.fragment.HomeFragment
import com.example.client.fragment.LoginFragment
import com.example.sever.IMessageService
import com.example.sever.model.ConversationItem
import com.example.sever.model.Message
import com.example.sever.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),
    ChatFragment.ChatFragmentCallBack,
    HomeFragment.HomeFragmentCallBack,
    LoginFragment.LoginFragmentCallBack {

    companion object {
        const val SERVER_PACKAGE = "com.example.sever"
        const val SERVICE_ACTION = "chat_service"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: ClientBroadcastReceiver

    private var iMessageService: IMessageService? = null
    private var isServiceConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindService()
        receiverRegistry()
    }

    override fun onStart() {
        super.onStart()
        if (!isServiceConnected) {
            bindService()
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun receiverRegistry() {
        receiver = ClientBroadcastReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(ClientBroadcastReceiver.ACTION_UPDATE_CHAT)
            addAction(ClientBroadcastReceiver.ACTION_UPDATE_CONVERSATION)
        }
        registerReceiver(receiver, intentFilter)
    }

    private fun bindService() {
        val intent = Intent()
        intent.`package` = SERVER_PACKAGE
        intent.action = SERVICE_ACTION
        bindService(intent, mConnection, BIND_AUTO_CREATE)
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            iMessageService = IMessageService.Stub.asInterface(service)
            isServiceConnected = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            iMessageService = null
            isServiceConnected = false
        }
    }

    override fun onDestroy() {
        unbindService(mConnection)
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun getMessageByConversation(conversationId: Int): Flow<List<Message>> {
        val messages = MutableSharedFlow<List<Message>>()
        lifecycleScope.launch(Dispatchers.IO) {
            val result =
                iMessageService?.getMessageByConversation(conversationId) ?: mutableListOf()
            withContext(Dispatchers.Main) {
                messages.emit(result)
            }
        }
        return messages
    }

    override fun sendMessage(input: String, conversationId: Int, receiveId: Int, sendId: Int) {
        iMessageService?.sendMesage(input, conversationId, receiveId, sendId)
    }

    override fun deleteChatFromChat(conversationId: Int) {
        iMessageService?.deleteChat(conversationId)
    }

    override fun getAllConversationByUserId(userId: Int): Flow<List<ConversationItem>> {
        val conversations = MutableSharedFlow<List<ConversationItem>>()
        lifecycleScope.launch(Dispatchers.IO) {
            val result = iMessageService?.getAllConversationByUserId(userId) ?: mutableListOf()
            withContext(Dispatchers.Main) {
                conversations.emit(result)
            }
        }
        return conversations
    }

    override fun deleteChatFromHome(conversationId: Int) {
        iMessageService?.deleteFriend(conversationId)
    }

    override fun getUser(userId: Int): Flow<User?> {
        val user = MutableSharedFlow<User?>()
        lifecycleScope.launch(Dispatchers.IO) {
            val result = iMessageService?.getUser(userId)
            withContext(Dispatchers.Main) {
                user.emit(result)
            }
        }
        return user
    }
}