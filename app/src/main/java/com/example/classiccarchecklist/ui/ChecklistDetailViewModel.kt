package com.example.classiccarchecklist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.classiccarchecklist.data.CarChecklist
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
    
    init {
        loadChecklist()
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

