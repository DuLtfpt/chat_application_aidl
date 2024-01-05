package com.example.sever.di

import android.app.Application
import com.example.sever.dal.ChatAppDatabase
import com.example.sever.repository.ConversationRepository
import com.example.sever.repository.MessageRepository
import com.example.sever.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideChatApplication(application: Application): ChatAppServerApplication {
        return application as ChatAppServerApplication
    }
    @Provides
    @Singleton
    fun provideDatabase(application: ChatAppServerApplication) = ChatAppDatabase.getDatabase(application)

    @Provides
    @Singleton
    fun provideUserRepository(database: ChatAppDatabase) = UserRepository(database.getUserDao())

    @Provides
    @Singleton
    fun provideMessageRepository(database: ChatAppDatabase) = MessageRepository(database.getMessageDao())

    @Provides
    @Singleton
    fun provideConversation(database: ChatAppDatabase) = ConversationRepository(database.getConversationDao())

}