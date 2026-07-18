package com.example.myapplication2.ui.posts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication2.ui.postListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    onOpenPost: (Int) -> Unit
) {
    val vm = postListViewModel()
    val posts by vm.posts.collectAsState()
    val refreshing by vm.refreshing.collectAsState()
    val message by vm.message.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "帖子") },
                actions = {
                    TextButton(onClick = vm::refresh, enabled = !refreshing) {
                        Text(text = if (refreshing) "刷新中" else "刷新")
                    }
                    TextButton(onClick = vm::logout) {
                        Text(text = "退出")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (message != null) {
                Text(text = message ?: "", modifier = Modifier.padding(horizontal = 16.dp))
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(posts, key = { it.id }) { post ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = post.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        supportingContent = {
                            Text(
                                text = post.body,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenPost(post.id) }
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}
