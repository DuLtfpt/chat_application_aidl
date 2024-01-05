package com.example.sever.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sever.model.Message
import com.example.sever.repository.ConversationRepository
import com.example.sever.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepo: MessageRepository,
    private val convRepo: ConversationRepository
) : ViewModel() {
    fun getMessageByConversation(conversationId: Int): Flow<List<Message>> {
        return chatRepo.getMessageByConversation(conversationId)
    }

    fun sendMessage(input: String, conversationId: Int, receiveId: Int, sendId: Int) {
        val content = input.trim()
        if (content.isEmpty()) {
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
        insertMessage(message)
        updateConversation(conversationId, messageId)
    }

    private fun insertMessage(message: Message) = viewModelScope.launch(Dispatchers.IO) {
        chatRepo.insert(message)
    }

    private fun updateConversation(conversationId: Int, messageId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            convRepo.updateConversation(conversationId, messageId)
        }

    fun deleteChat(conversationId: Int) = viewModelScope.launch(Dispatchers.IO) {
        chatRepo.deleteMessageConversation(conversationId)
    }
}