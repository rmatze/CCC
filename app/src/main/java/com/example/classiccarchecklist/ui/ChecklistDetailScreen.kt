package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classiccarchecklist.data.ChecklistRepository
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistDetailScreen(
    repository: ChecklistRepository,
    checklistId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ChecklistDetailViewModel = viewModel(
        factory = ChecklistDetailViewModelFactory(repository, checklistId)
    )
    
    val checklist by viewModel.checklist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var showDatePicker by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var editedCarInfo by remember { mutableStateOf("") }
    var editedVin by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    
    // Initialize edit fields when checklist loads
    LaunchedEffect(checklist) {
        if (checklist != null && !isEditing) {
            editedCarInfo = checklist!!.carInfo
            editedVin = checklist!!.vin
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checklist Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (isEditing) {
                        TextButton(
                            onClick = {
                                isEditing = false
                                // Reset to original values
                                checklist?.let {
                                    editedCarInfo = it.carInfo
                                    editedVin = it.vin
                                }
                            }
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                isSaving = true
                            },
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Save")
                            }
                        }
                    } else {
                        TextButton(
                            onClick = {
                                isEditing = true
                            }
                        ) {
                            Text("Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = onNavigateBack) {
                            Text("Go Back")
                        }
                    }
                }
            }
            checklist != null -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Date field (read-only for now)
                    OutlinedTextField(
                        value = formatDateForDisplay(checklist!!.date),
                        onValueChange = { },
                        label = { Text("Date") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date"
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    // Car Information field
                    OutlinedTextField(
                        value = if (isEditing) editedCarInfo else checklist!!.carInfo,
                        onValueChange = { 
                            if (isEditing) {
                                editedCarInfo = it
                                viewModel.updateCarInfo(it)
                            }
                        },
                        label = { Text("Car Information") },
                        placeholder = { Text("Enter car details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        minLines = 3,
                        maxLines = 5,
                        enabled = isEditing
                    )
                    
                    // VIN field
                    OutlinedTextField(
                        value = if (isEditing) editedVin else checklist!!.vin,
                        onValueChange = { 
                            if (isEditing) {
                                editedVin = it
                                viewModel.updateVin(it)
                            }
                        },
                        label = { Text("VIN Number") },
                        placeholder = { Text("Enter VIN number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        enabled = isEditing
                    )
                    
                    // Info text
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Checklist items will be displayed here in Phase 4.",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
    
    // Handle save
    LaunchedEffect(isSaving) {
        if (isSaving) {
            val success = viewModel.saveChanges()
            if (success) {
                isEditing = false
            }
            isSaving = false
        }
    }
}

private fun formatDateForDisplay(date: Date): String {
    val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}

