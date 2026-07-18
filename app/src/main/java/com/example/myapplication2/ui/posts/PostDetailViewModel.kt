package com.example.myapplication2.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication2.data.repository.PostsRepository
import com.example.myapplication2.model.Post
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PostDetailViewModel(
    postsRepository: PostsRepository,
    postId: Int
) : ViewModel() {
    val post: StateFlow<Post?> =
        postsRepository.observePost(postId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
