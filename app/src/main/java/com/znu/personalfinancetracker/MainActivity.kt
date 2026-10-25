package com.znu.personalfinancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.znu.personalfinancetracker.ui.theme.PersonalFinanceTrackerTheme
import com.znu.personalfinancetracker.ui.view.main.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                PersonalFinanceTrackerTheme {
                    MainView()
                }
            }
        }
    }
}