package com.example.sever.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.sever.model.Conversation
import com.example.sever.model.ConversationItem
import kotlinx.coroutines.flow.Flow


@Dao
interface ConversationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Transaction
    fun insert(conversations: List<Conversation>)

    /***
     * SELECT a.conversation_id, user_id2, name,content,timestamp FROM
     * (SELECT user_id2,name, conversation_id, latest_message_id FROM conversation, user
     * WHERE user_id2 = user_id AND user_id1= :userId AND user_id2 NOT IN (:userId)
     * GROUP BY user_id2) AS a
     * LEFT OUTER JOIN
     * message
     * ON a.latest_message_id = message_id
     * ORDER BY timestamp DESC
     */
    @Query(
        "SELECT a.conversation_id,user_id2,name,content,timestamp FROM\n" +
                "(SELECT user_id2,name, conversation_id, latest_message_id FROM conversation, user\n" +
                "WHERE user_id2 = user_id AND user_id1= :userId AND user_id2 NOT IN (:userId)\n" +
                "GROUP BY user_id2) AS a\n" +
                "LEFT OUTER JOIN\n" +
                "message\n" +
                "ON a.latest_message_id = message_id\n" +
                "ORDER BY timestamp DESC"
    )
    fun getAllConversationItem(userId: Int): Flow<List<ConversationItem>>

    @Query(
        "SELECT a.conversation_id,user_id2,name,content,timestamp FROM\n" +
                "(SELECT user_id2,name, conversation_id, latest_message_id FROM conversation, user\n" +
                "WHERE user_id2 = user_id AND user_id1= :userId AND user_id2 NOT IN (:userId)\n" +
                "GROUP BY user_id2) AS a\n" +
                "LEFT OUTER JOIN\n" +
                "message\n" +
                "ON a.latest_message_id = message_id\n" +
                "ORDER BY timestamp DESC"
    )
    fun getAllConversationItemBlock(userId: Int): List<ConversationItem>

    @Query("DELETE FROM conversation WHERE conversation_id =:conversationId")
    @Transaction
    fun delete(conversationId: Int)

    @Query("UPDATE conversation SET latest_message_id = :messageId WHERE conversation_id = :conversationId")
    fun update(conversationId: Int, messageId: String)
}