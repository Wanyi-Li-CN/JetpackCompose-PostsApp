package com.example.myapplication2.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication2.LocalAppContainer
import com.example.myapplication2.ui.auth.LoginScreen
import com.example.myapplication2.ui.auth.RegisterScreen
import com.example.myapplication2.ui.posts.PostDetailScreen
import com.example.myapplication2.ui.posts.PostListScreen
import kotlinx.coroutines.flow.map

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash
    ) {
        composable(Routes.Splash) {
            val container = LocalAppContainer.current
            val session by container.authRepository.loggedInUserId
                .map { it ?: -1L }
                .collectAsState(initial = -2L)

            LaunchedEffect(session) {
                when (session) {
                    -2L -> Unit
                    -1L -> navController.navigate(Routes.Login) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                    else -> navController.navigate(Routes.Posts) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                }
            }

            Scaffold(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "启动中...",
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }

        composable(Routes.Login) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Routes.Posts) {
                        popUpTo(Routes.Login) { inclusive = true }
                    }
                },
                onGoRegister = { navController.navigate(Routes.Register) }
            )
        }

        composable(Routes.Register) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(Routes.Posts) {
                        popUpTo(Routes.Register) { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.Posts) {
            val container = LocalAppContainer.current
            val loggedIn by container.authRepository.loggedInUserId
                .map { it != null }
                .collectAsState(initial = true)

            LaunchedEffect(loggedIn) {
                if (!loggedIn) {
                    navController.navigate(Routes.Login) {
                        popUpTo(Routes.Posts) { inclusive = true }
                    }
                }
            }

            PostListScreen(
                onOpenPost = { postId -> navController.navigate(Routes.postDetail(postId)) }
            )
        }

        composable(
            route = Routes.PostDetail,
            arguments = listOf(navArgument(Routes.ArgPostId) { type = NavType.IntType })
        ) { entry ->
            val postId = entry.arguments?.getInt(Routes.ArgPostId) ?: 0
            PostDetailScreen(
                postId = postId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
