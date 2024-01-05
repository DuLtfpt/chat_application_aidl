package com.example.sever.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.sever.IMessageService
import com.example.sever.model.ConversationItem
import com.example.sever.model.Message
import com.example.sever.model.User
import com.example.sever.repository.ConversationRepository
import com.example.sever.repository.MessageRepository
import com.example.sever.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MessageService : Service() {
    @Inject
    lateinit var userRepo: UserRepository

    @Inject
    lateinit var messRepo: MessageRepository

    @Inject
    lateinit var convRepo: ConversationRepository

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    private val mBinder = object : IMessageService.Stub() {

        override fun getMessageByConversation(conversationId: Int): List<Message> {
            return messRepo.getMessageByConversationBlock(conversationId)
        }

        override fun deleteChat(conversationId: Int) {
            messRepo.deleteMessageConversation(conversationId)
        }

        override fun deleteFriend(conversationId: Int) {
            messRepo.deleteMessageConversation(conversationId)
            convRepo.deleteConversation(conversationId)
        }

        override fun sendMesage(input: String?, conversationId: Int, receiveId: Int, sendId: Int) {
            insertMessage(input, conversationId, receiveId, sendId)
        }

        override fun getAllConversationByUserId(userId: Int): List<ConversationItem> {
            return convRepo.getAllConversationByUserIdBlock(userId)
        }

        override fun getUser(userId: Int): User? {
            return userRepo.getUser(userId)
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }


    fun insertMessage(input: String?, conversationId: Int, receiveId: Int, sendId: Int) {
        val content = input?.trim()
        if (content.isNullOrEmpty()) {
            return
        }
        val messageId = UUID.randomUUID().toString()
        val message = Message(
            messageId,
            conversationId,
            content,
            sendId,
            receiveId,
            System.currentTimeMillis()
        )
        messRepo.insert(message)
        convRepo.updateConversation(conversationId, messageId)
    }
}