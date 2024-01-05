package com.example.sever.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sever.model.User
import com.example.sever.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: UserRepository) :
    ViewModel() {

    private var _user = MutableSharedFlow<User?>()

    fun getUser(userId: Int): MutableSharedFlow<User?> {
        queryUser(userId)
        return _user
    }

    private fun queryUser(userId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val result = repository.getUser(userId)
        withContext(Dispatchers.Main) {
            _user.apply {
                emit(result)
            }
        }
    }
}