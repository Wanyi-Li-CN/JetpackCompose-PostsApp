package com.example.myapplication2.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication2.LocalAppContainer
import com.example.myapplication2.ui.auth.AuthViewModel
import com.example.myapplication2.ui.posts.PostDetailViewModel
import com.example.myapplication2.ui.posts.PostListViewModel

@Composable
fun authViewModel(): AuthViewModel {
    val container = LocalAppContainer.current
    return viewModel(
        factory = simpleFactory { AuthViewModel(container.authRepository) }
    )
}

@Composable
fun postListViewModel(): PostListViewModel {
    val container = LocalAppContainer.current
    return viewModel(
        factory = simpleFactory { PostListViewModel(container.postsRepository, container.authRepository) }
    )
}

@Composable
fun postDetailViewModel(postId: Int): PostDetailViewModel {
    val container = LocalAppContainer.current
    return viewModel(
        key = "postDetail:$postId",
        factory = simpleFactory { PostDetailViewModel(container.postsRepository, postId) }
    )
}

private inline fun <reified T : ViewModel> simpleFactory(crossinline create: () -> T): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            return create() as VM
        }
    }

