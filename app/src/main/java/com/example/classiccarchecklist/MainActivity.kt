package com.example.classiccarchecklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.classiccarchecklist.data.ChecklistDatabase
import com.example.classiccarchecklist.data.ChecklistRepository
import com.example.classiccarchecklist.navigation.NavGraph
import com.example.classiccarchecklist.ui.ChecklistListViewModel
import com.example.classiccarchecklist.ui.ChecklistListViewModelFactory
import com.example.classiccarchecklist.ui.theme.ClassicCarChecklistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize database and repository
        val database = ChecklistDatabase.getDatabase(applicationContext)
        val repository = ChecklistRepository(
            checklistDao = database.carChecklistDao(),
            itemDao = database.checklistItemDao()
        )
        val viewModelFactory = ChecklistListViewModelFactory(repository)
        
        setContent {
            ClassicCarChecklistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: ChecklistListViewModel = viewModel(
                        factory = viewModelFactory
                    )
                    
                    NavGraph(
                        navController = navController,
                        checklistListViewModel = viewModel,
                        repository = repository
                    )
                }
            }
        }
    }
}
