package com.example.classiccarchecklist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.window.Dialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classiccarchecklist.data.ChecklistRepository
import com.example.classiccarchecklist.data.ChecklistSection
import java.text.SimpleDateFormat
import java.util.*
import java.util.TimeZone

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
    val checklistItems by viewModel.checklistItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var isEditing by remember { mutableStateOf(false) }
    var editedCarInfo by remember { mutableStateOf("") }
    var editedVin by remember { mutableStateOf("") }
    var editedDate by remember { mutableStateOf<Date?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var selectedSection by remember { mutableStateOf<ChecklistSection?>(null) }
    
    // Initialize edit fields when checklist loads
    LaunchedEffect(checklist) {
        if (checklist != null && !isEditing) {
            editedCarInfo = checklist!!.carInfo
            editedVin = checklist!!.vin
            editedDate = checklist!!.date
        }
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error snackbar when error occurs
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Long
            )
            viewModel.clearError()
        }
    }
    
    // Calculate progress
    val countableItems = checklistItems.filter { !it.isOptional }
    val totalItems = countableItems.size
    val completedItems = countableItems.count { it.value != null }
    val progressPercent = if (totalItems > 0) (completedItems * 100f / totalItems).toInt() else 0
    val progress = if (totalItems > 0) completedItems.toFloat() / totalItems.toFloat() else 0f
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = checklist?.carInfo?.takeIf { it.isNotEmpty() } ?: "Checklist",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (checklist != null) {
                            Text(
                                text = "${formatDateForDisplay(checklist!!.date)} · VIN ${checklist!!.vin.takeIf { it.isNotEmpty() } ?: "N/A"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
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
                                checklist?.let {
                                    editedCarInfo = it.carInfo
                                    editedVin = it.vin
                                    editedDate = it.date
                                }
                            }
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = { isSaving = true },
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
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$completedItems / $totalItems items completed",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (progressPercent == 100) {
                        Button(onClick = { /* TODO: complete checklist */ }) {
                            Text("Complete checklist")
                        }
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                val scrollState = rememberScrollState()
                
                // Reset scroll when section changes
                LaunchedEffect(selectedSection) {
                    scrollState.animateScrollTo(0)
                }
                
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Pinned progress card (static)
                    ProgressCard(
                        completed = completedItems,
                        total = totalItems,
                        progress = progress,
                        progressPercent = progressPercent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    
                    // Section tabs (static)
                    SectionTabs(
                        items = checklistItems,
                        selectedSection = selectedSection,
                        onSectionSelected = { selectedSection = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    
                    // Scrollable content area
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
                        // Edit fields (when editing)
                        if (isEditing) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Date field
                                OutlinedTextField(
                                    value = editedDate?.let { formatDateForDisplay(it) } ?: "",
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
                                
                                OutlinedTextField(
                                    value = editedCarInfo,
                                    onValueChange = {
                                        editedCarInfo = it
                                        viewModel.updateCarInfo(it)
                                    },
                                    label = { Text("Car Information") },
                                    placeholder = { Text("Enter car details") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 120.dp),
                                    minLines = 3,
                                    maxLines = 5
                                )
                                
                                OutlinedTextField(
                                    value = editedVin,
                                    onValueChange = {
                                        if (it.length <= 17) {
                                            editedVin = it.uppercase()
                                            viewModel.updateVin(it.uppercase())
                                        }
                                    },
                                    label = { Text("VIN Number") },
                                    placeholder = { Text("Enter VIN number") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    supportingText = {
                                        if (editedVin.isNotEmpty()) {
                                            Text("${editedVin.length}/17 characters")
                                        }
                                    }
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        
                        // Checklist items (filtered by selected section)
                        val filteredItems = if (selectedSection != null) {
                            checklistItems.filter { it.section == selectedSection }
                        } else {
                            checklistItems
                        }
                        
                        ChecklistItemsView(
                            items = filteredItems,
                            onItemValueChanged = { itemId, value ->
                                viewModel.updateItemValue(itemId, value)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(80.dp)) // Space for bottom bar
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
    
    // Date picker dialog
    if (showDatePicker && editedDate != null) {
        ChecklistDatePickerDialog(
            initialDate = editedDate!!,
            onDateSelected = { selectedDate: Date ->
                editedDate = selectedDate
                viewModel.updateDate(selectedDate)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun ProgressCard(
    completed: Int,
    total: Int,
    progress: Float,
    progressPercent: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$completed / $total",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$progressPercent% complete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(64.dp),
                strokeWidth = 6.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun SectionTabs(
    items: List<com.example.classiccarchecklist.data.ChecklistItemDomain>,
    selectedSection: ChecklistSection?,
    onSectionSelected: (ChecklistSection?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // "All" option
        FilterChip(
            selected = selectedSection == null,
            onClick = { onSectionSelected(null) },
            label = { Text("All") }
        )
        
        // Section chips
        ChecklistSection.values().forEach { section ->
            val sectionItems = items.filter { it.section == section && !it.isOptional }
            val completed = sectionItems.count { it.value != null }
            val total = sectionItems.size
            
            FilterChip(
                selected = selectedSection == section,
                onClick = { onSectionSelected(section) },
                label = {
                    Column {
                        Text(section.displayName)
                        if (total > 0) {
                            Text(
                                text = "$completed/$total",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChecklistDatePickerDialog(
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
                    fontWeight = FontWeight.Bold
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
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(date)
}
