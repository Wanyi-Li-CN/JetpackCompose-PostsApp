package com.example.myapplication2.ui.navigation

object Routes {
    const val Splash = "splash"
    const val Login = "login"
    const val Register = "register"
    const val Posts = "posts"

    const val ArgPostId = "postId"
    const val PostDetail = "post/{$ArgPostId}"

    fun postDetail(postId: Int): String = "post/$postId"
}

