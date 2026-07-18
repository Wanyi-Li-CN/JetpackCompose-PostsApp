package com.example.myapplication2.data.local

import android.content.Context
import com.example.myapplication2.model.Post
import com.example.myapplication2.model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalStore(
    private val appContext: Context,
    private val gson: Gson = Gson()
) {
    private val usersFile: File = File(appContext.filesDir, "users.json")
    private val postsFile: File = File(appContext.filesDir, "posts.json")

    suspend fun loadUsers(): List<User> = withContext(Dispatchers.IO) {
        if (!usersFile.exists()) return@withContext emptyList()
        val json = usersFile.readText()
        if (json.isBlank()) return@withContext emptyList()
        val type = object : TypeToken<List<User>>() {}.type
        gson.fromJson<List<User>>(json, type) ?: emptyList()
    }

    suspend fun saveUsers(users: List<User>) = withContext(Dispatchers.IO) {
        usersFile.writeText(gson.toJson(users))
    }

    suspend fun loadPosts(): List<Post> = withContext(Dispatchers.IO) {
        if (!postsFile.exists()) return@withContext emptyList()
        val json = postsFile.readText()
        if (json.isBlank()) return@withContext emptyList()
        val type = object : TypeToken<List<Post>>() {}.type
        gson.fromJson<List<Post>>(json, type) ?: emptyList()
    }

    suspend fun savePosts(posts: List<Post>) = withContext(Dispatchers.IO) {
        postsFile.writeText(gson.toJson(posts))
    }
}

