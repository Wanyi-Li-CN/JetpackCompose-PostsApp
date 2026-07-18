package com.example.myapplication2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.example.myapplication2.ui.theme.MyApplication2Theme
import com.example.myapplication2.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplication2Theme {
                val container = (application as MyApplication2Application).container
                CompositionLocalProvider(LocalAppContainer provides container) {
                    AppNavGraph()
                }
            }
        }
    }
}
