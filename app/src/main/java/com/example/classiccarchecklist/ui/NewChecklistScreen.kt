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
fun NewChecklistScreen(
    repository: ChecklistRepository,
    onNavigateBack: () -> Unit,
    onSaveComplete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: NewChecklistViewModel = viewModel(
        factory = NewChecklistViewModelFactory(repository)
    )
    
    val date by viewModel.date.collectAsState()
    val carInfo by viewModel.carInfo.collectAsState()
    val vin by viewModel.vin.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val saveError by viewModel.saveError.collectAsState()
    
    var showDatePicker by remember { mutableStateOf(false) }
    var shouldSave by remember { mutableStateOf(false) }
    
    // Handle save completion
    LaunchedEffect(shouldSave) {
        if (shouldSave) {
            val checklistId = viewModel.saveChecklist()
            checklistId?.let {
                onSaveComplete(it)
            }
            shouldSave = false
        }
    }
    
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
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            shouldSave = true
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Save & Continue")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error message
            saveError?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            
            // Date field
            OutlinedTextField(
                value = formatDateForDisplay(date),
                onValueChange = { },
                label = { Text("Date") },
                readOnly = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = { showDatePicker = true }) {
                        Text("Change")
                    }
                }
            )
            
            // Car Information field (multi-line)
            OutlinedTextField(
                value = carInfo,
                onValueChange = { viewModel.updateCarInfo(it) },
                label = { Text("Car Information") },
                placeholder = { Text("Enter car details (e.g., 1965 Mustang, Red, Convertible)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                minLines = 3,
                maxLines = 5
            )
            
            // VIN field
            OutlinedTextField(
                value = vin,
                onValueChange = { 
                    // Limit VIN to 17 characters (standard VIN length)
                    if (it.length <= 17) {
                        viewModel.updateVin(it.uppercase())
                    }
                },
                label = { Text("VIN Number") },
                placeholder = { Text("Enter VIN number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                supportingText = {
                    if (vin.isNotEmpty()) {
                        Text("${vin.length}/17 characters")
                    }
                }
            )
            
            // Info text
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "After saving, you'll be able to fill out the detailed checklist.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
    
    // Simple date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            initialDate = date,
            onDateSelected = { selectedDate ->
                viewModel.updateDate(selectedDate)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
fun DatePickerDialog(
    initialDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = remember { Calendar.getInstance().apply { time = initialDate } }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Date") },
        text = {
            Column {
                // Simple date input fields
                // For a better UX, you could use a proper date picker library
                // For now, we'll use a simple approach
                OutlinedTextField(
                    value = "${selectedMonth + 1}/${selectedDay}/${selectedYear}",
                    onValueChange = { },
                    label = { Text("Date (MM/DD/YYYY)") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Note: Full date picker will be enhanced in future update",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newCalendar = Calendar.getInstance().apply {
                        set(selectedYear, selectedMonth, selectedDay)
                    }
                    onDateSelected(newCalendar.time)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun formatDateForDisplay(date: Date): String {
    val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}
