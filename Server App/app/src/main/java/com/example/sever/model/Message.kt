package com.example.sever.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("message")
data class Message(
    @PrimaryKey
    @ColumnInfo("message_id")
    val id: String,
    @ColumnInfo("conversation_id")
    val conversationId: Int,
    @ColumnInfo("content")
    val content: String,
    @ColumnInfo("send_id")
    val sendId: Int,
    @ColumnInfo("receive_id")
    val receiveId: Int,
    @ColumnInfo("timestamp")
    val timestamp: Long
) : Parcelable
