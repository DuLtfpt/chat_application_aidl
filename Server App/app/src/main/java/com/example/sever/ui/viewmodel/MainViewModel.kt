package com.example.sever.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sever.model.Conversation
import com.example.sever.model.User
import com.example.sever.repository.ConversationRepository
import com.example.sever.repository.MessageRepository
import com.example.sever.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val conRepo: ConversationRepository,
    private val messRepo: MessageRepository
) : ViewModel() {
    fun insertDummyData() = viewModelScope.launch(Dispatchers.IO) {
        var conversationId = 0
        val users = mutableListOf<User>()
        val conversations = mutableListOf<Conversation>()
        for (i in 1..20) {
            users.add(User(i, "User $i"))
            for (j in 1..20) {
                if (j <= i) continue
                conversations.add(Conversation(conversationId, i, j, null))
                conversations.add(Conversation(conversationId, j, i, null))
                conversationId++
            }
        }
        userRepo.insert(users)
        conRepo.insert(conversations)
    }
}