package com.example.sever.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val id: String,
    val conversationId: Int,
    val content: String,
    val sendId: Int,
    val receiveId: Int,
    val timestamp: Long
) : Parcelable
