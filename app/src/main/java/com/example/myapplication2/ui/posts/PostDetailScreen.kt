package com.example.myapplication2.ui.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication2.ui.postDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Int,
    onBack: () -> Unit
) {
    val vm = postDetailViewModel(postId)
    val post by vm.post.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "详情") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(text = "返回")
                    }
                }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            if (post == null) {
                Text(text = "加载中...")
            } else {
                Text(text = post?.title.orEmpty())
                Text(text = "")
                Text(text = post?.body.orEmpty())
            }
        }
    }
}
