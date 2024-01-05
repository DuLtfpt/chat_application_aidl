package com.example.sever.repository

import com.example.sever.dao.ConversationDao
import com.example.sever.model.Conversation
import javax.inject.Inject

class ConversationRepository @Inject constructor(private val dao: ConversationDao) {
    fun getAllConversationByUserId(userId: Int) = dao.getAllConversationItem(userId)

    fun getAllConversationByUserIdBlock(userId: Int) = dao.getAllConversationItemBlock(userId)

    fun insert(conversations: List<Conversation>) = dao.insert(conversations)

    fun deleteConversation(conversationId: Int) = dao.delete(conversationId)

    fun updateConversation(conversationId: Int, messageId: String) =
        dao.update(conversationId, messageId)
}