package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classiccarchecklist.data.ChecklistRepository
import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    initialDate: Date,
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate.time,
        yearRange = IntRange(1900, Calendar.getInstance().get(Calendar.YEAR) + 1)
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                // DatePicker returns UTC midnight, extract the date components
                                // and create a new date in local timezone to avoid day offset
                                val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                    timeInMillis = millis
                                }
                                
                                // Create a new date in local timezone with the same year/month/day
                                val localCalendar = Calendar.getInstance().apply {
                                    set(Calendar.YEAR, utcCalendar.get(Calendar.YEAR))
                                    set(Calendar.MONTH, utcCalendar.get(Calendar.MONTH))
                                    set(Calendar.DAY_OF_MONTH, utcCalendar.get(Calendar.DAY_OF_MONTH))
                                    set(Calendar.HOUR_OF_DAY, 12) // Use noon to avoid timezone edge cases
                                    set(Calendar.MINUTE, 0)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }
                                onDateSelected(localCalendar.time)
                            }
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

private fun formatDateForDisplay(date: Date): String {
    val formatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}
