package com.example.pw3_4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.pw3_4.composables.BasicApp
import com.example.pw3_4.ui.theme.Pw3_4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Pw3_4Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BasicApp()
                }
            }
        }
    }
}