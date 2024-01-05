package com.example.sever.repository

import com.example.sever.dao.UserDao
import com.example.sever.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(private val dao: UserDao) {
    fun insert(user: List<User>) = dao.insertUser(user)
    fun getUser(userId: Int) = dao.getUser(userId)
}