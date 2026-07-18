package com.example.myapplication2

import android.app.Application

class MyApplication2Application : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

