package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.data.TradingDatabase
import com.example.data.TradingRepository
import com.example.ui.TradingViewModel
import com.example.ui.TradingViewModelFactory
import com.example.ui.VertTradesApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Room DB & Repository
        val database = TradingDatabase.getDatabase(applicationContext)
        val repository = TradingRepository(database.tradingDao())

        // Initialize Socratic Trading ViewModel cleanly via provider
        val viewModel = ViewModelProvider(
            this,
            TradingViewModelFactory(application, repository)
        )[TradingViewModel::class.java]

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VertTradesApp(viewModel = viewModel)
                }
            }
        }
    }
}
