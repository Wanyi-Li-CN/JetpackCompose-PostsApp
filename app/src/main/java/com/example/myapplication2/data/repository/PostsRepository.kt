package com.example.myapplication2.data.repository

import com.example.myapplication2.data.local.LocalStore
import com.example.myapplication2.data.remote.ApiService
import com.example.myapplication2.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PostsRepository(
    private val apiService: ApiService,
    private val localStore: LocalStore
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    init {
        scope.launch {
            _posts.value = localStore.loadPosts()
        }
    }

    fun observePosts(): Flow<List<Post>> = posts

    fun observePost(id: Int): Flow<Post?> = posts.map { list -> list.firstOrNull { it.id == id } }

    suspend fun refresh(): Result<Unit> {
        return runCatching {
            val remote = apiService.getPosts()
            val posts = remote.map { dto ->
                Post(
                    id = dto.id,
                    userId = dto.userId,
                    title = dto.title,
                    body = dto.body
                )
            }
            _posts.value = posts
            localStore.savePosts(posts)
        }
    }
}
