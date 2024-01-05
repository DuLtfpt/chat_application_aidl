package com.example.sever.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sever.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    @Query("SELECT * FROM message WHERE conversation_id =:conversationId ORDER BY timestamp ASC")
    fun getMessageByConversation(conversationId: Int): Flow<List<Message>>

    @Query("DELETE FROM message WHERE conversation_id =:conversationId")
    fun deleteConversation(conversationId: Int)

    @Query("SELECT * FROM message WHERE conversation_id =:conversationId ORDER BY timestamp ASC")
    fun getMessageByConversationBlock(conversationId: Int): List<Message>
}