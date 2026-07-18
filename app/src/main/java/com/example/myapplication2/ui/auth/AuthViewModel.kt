package com.example.myapplication2.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _busy = MutableStateFlow(false)
    val busy = _busy.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val loggedInUserId: StateFlow<Long?> =
        authRepository.loggedInUserId.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val loggedIn: StateFlow<Boolean> =
        loggedInUserId.map { it != null }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun updateUsername(value: String) {
        _username.value = value
    }

    fun updatePassword(value: String) {
        _password.value = value
    }

    fun clearMessage() {
        _message.value = null
    }

    fun login() {
        if (_busy.value) return
        _busy.value = true
        _message.value = null
        viewModelScope.launch {
            val result = authRepository.login(_username.value, _password.value)
            if (result.isFailure) {
                _message.value = result.exceptionOrNull()?.message ?: "登录失败"
            } else {
                _password.value = ""
            }
            _busy.value = false
        }
    }

    fun register() {
        if (_busy.value) return
        _busy.value = true
        _message.value = null
        viewModelScope.launch {
            val result = authRepository.register(_username.value, _password.value)
            if (result.isFailure) {
                _message.value = result.exceptionOrNull()?.message ?: "注册失败"
            } else {
                _password.value = ""
            }
            _busy.value = false
        }
    }
}

