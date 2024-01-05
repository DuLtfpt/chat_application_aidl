package com.example.client.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class ClientBroadcastReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_UPDATE_CHAT = "com.example.client.action.update_chat"
        const val ACTION_UPDATE_CONVERSATION = "com.example.client.action.update_conversation"
        const val ACTION_CHAT_FRAGMENT = "ChatFragment"
        const val ACTION_HOME_FRAGMENT = "HomeFragment"
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_UPDATE_CHAT -> {
                val localIntent = Intent(ACTION_CHAT_FRAGMENT)
                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
            }

            ACTION_UPDATE_CONVERSATION -> {
                val localIntent = Intent(ACTION_HOME_FRAGMENT)
                LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent)
            }
        }
    }
}