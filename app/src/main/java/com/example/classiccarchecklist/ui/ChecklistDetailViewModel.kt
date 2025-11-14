package com.example.classiccarchecklist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.classiccarchecklist.data.CarChecklist
import com.example.classiccarchecklist.data.ChecklistItemDomain
import com.example.classiccarchecklist.data.ChecklistItemsDefinition
import com.example.classiccarchecklist.data.ChecklistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the checklist detail screen
 */
class ChecklistDetailViewModel(
    private val repository: ChecklistRepository,
    private val checklistId: Long
) : ViewModel() {
    
    private val _checklist = MutableStateFlow<CarChecklist?>(null)
    val checklist: StateFlow<CarChecklist?> = _checklist.asStateFlow()
    
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun clearError() {
        _error.value = null
    }
    
    private val _checklistItems = MutableStateFlow<List<ChecklistItemDomain>>(emptyList())
    val checklistItems: StateFlow<List<ChecklistItemDomain>> = _checklistItems.asStateFlow()
    
    init {
        loadChecklist()
        loadChecklistItems()
    }
    
    private fun loadChecklist() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val loadedChecklist = repository.getChecklistById(checklistId)
                if (loadedChecklist != null) {
                    _checklist.value = loadedChecklist
                } else {
                    _error.value = "Checklist not found"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load checklist"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateCarInfo(carInfo: String) {
        _checklist.value?.let { current ->
            _checklist.value = current.copy(carInfo = carInfo)
        }
    }
    
    fun updateVin(vin: String) {
        _checklist.value?.let { current ->
            _checklist.value = current.copy(vin = vin)
        }
    }
    
    private fun loadChecklistItems() {
        viewModelScope.launch {
            try {
                val existingItems = repository.getItemsByChecklistIdSuspend(checklistId)
                if (existingItems.isEmpty()) {
                    // Initialize with default items if none exist
                    val defaultItems = ChecklistItemsDefinition.getAllItems(checklistId)
                    repository.insertItems(defaultItems, checklistId)
                    _checklistItems.value = repository.getItemsByChecklistIdSuspend(checklistId)
                } else {
                    _checklistItems.value = existingItems
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load checklist items"
            }
        }
    }
    
    fun updateItemValue(itemId: Long, value: String?) {
        viewModelScope.launch {
            try {
                val updatedItems = _checklistItems.value.map { item ->
                    if (item.id == itemId) {
                        item.copy(value = value)
                    } else {
                        item
                    }
                }
                _checklistItems.value = updatedItems
                
                // Save to database immediately (auto-save)
                updatedItems.find { it.id == itemId }?.let { updatedItem ->
                    repository.updateItem(updatedItem, checklistId)
                    // Update lastModified timestamp
                    _checklist.value?.let { current ->
                        val updatedChecklist = current.copy(lastModified = java.util.Date())
                        _checklist.value = updatedChecklist
                        repository.updateChecklist(updatedChecklist)
                    }
                }
            } catch (e: Exception) {
                _error.value = "Failed to save: ${e.message}"
                // Clear error after 5 seconds
                kotlinx.coroutines.delay(5000)
                _error.value = null
            }
        }
    }
    
    suspend fun saveChanges(): Boolean {
        return try {
            _checklist.value?.let { checklist ->
                repository.updateChecklist(checklist)
                true
            } ?: false
        } catch (e: Exception) {
            _error.value = e.message ?: "Failed to save changes"
            false
        }
    }
}

/**
 * Factory for creating ChecklistDetailViewModel
 */
class ChecklistDetailViewModelFactory(
    private val repository: ChecklistRepository,
    private val checklistId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChecklistDetailViewModel(repository, checklistId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

