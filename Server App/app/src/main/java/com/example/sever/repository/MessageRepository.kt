package com.example.sever.repository

import com.example.sever.dao.MessageDao
import com.example.sever.model.Message
import javax.inject.Inject

class MessageRepository @Inject constructor(private val dao: MessageDao) {
    fun insert(message: Message) = dao.insert(message)

    fun getMessageByConversation(conversationId: Int) = dao.getMessageByConversation(conversationId)

    fun getMessageByConversationBlock(conversationId: Int) =
        dao.getMessageByConversationBlock(conversationId)

    fun deleteMessageConversation(conversationId: Int) = dao.deleteConversation(conversationId)
}