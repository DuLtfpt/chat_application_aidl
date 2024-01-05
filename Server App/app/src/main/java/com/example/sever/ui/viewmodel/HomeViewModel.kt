package com.example.sever.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sever.repository.ConversationRepository
import com.example.sever.repository.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ConversationRepository,
    private val messageRepository: MessageRepository
) :
    ViewModel() {
    fun getAllConversationByUserId(userId: Int) = repository.getAllConversationByUserId(userId)

    fun deleteChat(conversationId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteConversation(conversationId)
        messageRepository.deleteMessageConversation(conversationId)
    }
}