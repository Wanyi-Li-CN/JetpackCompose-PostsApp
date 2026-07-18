package com.example.myapplication2

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication2.data.local.LocalStore
import com.example.myapplication2.data.remote.ApiServiceFactory
import com.example.myapplication2.data.repository.AuthRepository
import com.example.myapplication2.data.repository.PostsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("AppContainer not provided")
}

class AppContainer(appContext: Context) {
    private val localStore = LocalStore(appContext)
    private val sessionStore = SessionStore(appContext)
    private val apiService = ApiServiceFactory.create()

    val authRepository = AuthRepository(
        localStore = localStore,
        sessionStore = sessionStore
    )

    val postsRepository = PostsRepository(
        apiService = apiService,
        localStore = localStore
    )
}

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionStore(private val appContext: Context) {
    private val keyUserId = longPreferencesKey("user_id")

    val loggedInUserId: Flow<Long?> =
        appContext.sessionDataStore.data
            .map { preferences -> preferences[keyUserId] }
            .distinctUntilChanged()

    suspend fun setLoggedInUserId(userId: Long) {
        appContext.sessionDataStore.edit { preferences ->
            preferences[keyUserId] = userId
        }
    }

    suspend fun clear() {
        appContext.sessionDataStore.edit { preferences ->
            preferences.remove(keyUserId)
        }
    }
}
