package com.example.sever.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConversationItem(
    @ColumnInfo("conversation_id")
    val conversationId: Int,
    @ColumnInfo("user_id2")
    val userId: Int,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("content")
    val lastMessage: String?,
    @ColumnInfo("timestamp")
    val timestamp: Long?
) : Parcelable
