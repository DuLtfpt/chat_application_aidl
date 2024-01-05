package com.example.sever.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.sever.model.User

@Dao
interface UserDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(users: List<User>)

    @Query("SELECT * FROM user WHERE user_id = :userId")
    fun getUser(userId: Int): User?
}