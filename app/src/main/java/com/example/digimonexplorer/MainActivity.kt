package com.example.digimonexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.digimonexplorer.data.remote.RetrofitClient
import com.example.digimonexplorer.data.repository.DigimonRepositoryImpl
import com.example.digimonexplorer.ui.ViewModelFactory
import com.example.digimonexplorer.ui.navigation.DigimonNavGraph
import com.example.digimonexplorer.ui.theme.DigimonExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = DigimonRepositoryImpl(RetrofitClient.apiService)
        val viewModelFactory = ViewModelFactory(repository)

        setContent {
            DigimonExplorerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DigimonNavGraph(viewModelFactory = viewModelFactory)
                }
            }
        }
    }
}
