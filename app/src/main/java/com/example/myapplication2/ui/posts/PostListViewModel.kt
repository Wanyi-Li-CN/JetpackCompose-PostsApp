package com.example.myapplication2.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.data.repository.AuthRepository
import com.example.myapplication2.data.repository.PostsRepository
import com.example.myapplication2.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postsRepository: PostsRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    val posts: StateFlow<List<Post>> =
        postsRepository.observePosts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _refreshing = MutableStateFlow(false)
    val refreshing = _refreshing.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    init {
        refresh()
    }

    fun clearMessage() {
        _message.value = null
    }

    fun refresh() {
        if (_refreshing.value) return
        _refreshing.value = true
        _message.value = null
        viewModelScope.launch {
            val result = postsRepository.refresh()
            if (result.isFailure) {
                _message.value = result.exceptionOrNull()?.message ?: "刷新失败"
            }
            _refreshing.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
