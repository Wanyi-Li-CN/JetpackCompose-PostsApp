package com.example.myapplication2.data.repository

import com.example.myapplication2.SessionStore
import com.example.myapplication2.data.local.LocalStore
import com.example.myapplication2.model.User
import java.security.MessageDigest

class AuthRepository(
    private val localStore: LocalStore,
    private val sessionStore: SessionStore
) {
    val loggedInUserId = sessionStore.loggedInUserId

    suspend fun register(username: String, password: String): Result<Unit> {
        val u = username.trim()
        if (u.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("用户名或密码不能为空"))
        }
        val users = localStore.loadUsers()
        if (users.any { it.username == u }) {
            return Result.failure(IllegalStateException("用户名已存在"))
        }
        val id = (users.maxOfOrNull { it.id } ?: 0L) + 1L
        localStore.saveUsers(
            users + User(
                id = id,
                username = u,
                passwordHash = sha256(password)
            )
        )
        sessionStore.setLoggedInUserId(id)
        return Result.success(Unit)
    }

    suspend fun login(username: String, password: String): Result<Unit> {
        val u = username.trim()
        if (u.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("用户名或密码不能为空"))
        }
        val user = localStore.loadUsers().firstOrNull { it.username == u }
            ?: return Result.failure(IllegalStateException("账号不存在"))
        if (user.passwordHash != sha256(password)) {
            return Result.failure(IllegalStateException("密码错误"))
        }
        sessionStore.setLoggedInUserId(user.id)
        return Result.success(Unit)
    }

    suspend fun logout() {
        sessionStore.clear()
    }

    private fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return digest.joinToString(separator = "") { b -> "%02x".format(b) }
    }
}
