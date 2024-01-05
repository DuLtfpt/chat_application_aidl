package com.example.sever.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConversationItem(
    val conversationId: Int,
    val userId: Int,
    val name: String,
    val lastMessage: String?,
    val timestamp: Long?
) : Parcelable
