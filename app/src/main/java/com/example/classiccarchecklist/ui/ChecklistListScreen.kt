package com.example.classiccarchecklist.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.classiccarchecklist.data.CarChecklist
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistListScreen(
    viewModel: ChecklistListViewModel,
    onAddChecklist: () -> Unit,
    onChecklistClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val checklists by viewModel.checklists.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Classic Car Checklists") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddChecklist,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new checklist"
                )
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            checklists.isEmpty() -> {
                EmptyState(
                    onAddChecklist = onAddChecklist,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = checklists,
                        key = { it.id }
                    ) { checklist ->
                        ChecklistItemCard(
                            checklist = checklist,
                            onClick = { onChecklistClick(checklist.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChecklistItemCard(
    checklist: CarChecklist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Date
            Text(
                text = formatDate(checklist.date),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Car Info
            if (checklist.carInfo.isNotEmpty()) {
                Text(
                    text = checklist.carInfo,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Text(
                    text = "No car information",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // VIN
            if (checklist.vin.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "VIN: ${checklist.vin}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EmptyState(
    onAddChecklist: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "No Checklists Yet",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Tap the + button to create your first classic car checklist",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onAddChecklist,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Checklist")
            }
        }
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}

