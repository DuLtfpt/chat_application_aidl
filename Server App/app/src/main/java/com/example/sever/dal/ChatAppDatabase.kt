package com.example.sever.dal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sever.dao.ConversationDao
import com.example.sever.dao.MessageDao
import com.example.sever.dao.UserDao
import com.example.sever.model.Conversation
import com.example.sever.model.Message
import com.example.sever.model.User

@Database(
    entities = [User::class, Conversation::class, Message::class],
    version = 1
)
abstract class ChatAppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getMessageDao(): MessageDao
    abstract fun getConversationDao(): ConversationDao

    companion object {
        private const val DATABASE_NAME = "chat_app_database"

        @Volatile
        private var INSTANCE: ChatAppDatabase? = null
        fun getDatabase(context: Context): ChatAppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChatAppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

