package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Placeholder screen for creating a new checklist
 * Will be fully implemented in Phase 3
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewChecklistScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Checklist") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "New Checklist Screen\n(Phase 3)",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

