package com.example.sever.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "conversation", primaryKeys = ["user_id1","user_id2"])
data class Conversation(
    @ColumnInfo("conversation_id")
    val conversationId: Int,
    @ColumnInfo("user_id1")
    val userId1: Int,
    @ColumnInfo("user_id2")
    val userId2: Int,
    @ColumnInfo("latest_message_id")
    val latestMessageId: String?
)