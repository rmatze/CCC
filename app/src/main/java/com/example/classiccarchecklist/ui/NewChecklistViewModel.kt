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
import java.util.Date

/**
 * ViewModel for the new checklist screen
 */
class NewChecklistViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {
    
    private val _date = MutableStateFlow(Date())
    val date: StateFlow<Date> = _date.asStateFlow()
    
    private val _carInfo = MutableStateFlow("")
    val carInfo: StateFlow<String> = _carInfo.asStateFlow()
    
    private val _vin = MutableStateFlow("")
    val vin: StateFlow<String> = _vin.asStateFlow()
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
    
    private val _saveError = MutableStateFlow<String?>(null)
    val saveError: StateFlow<String?> = _saveError.asStateFlow()
    
    fun updateCarInfo(info: String) {
        _carInfo.value = info
    }
    
    fun updateVin(vin: String) {
        _vin.value = vin
    }
    
    fun updateDate(date: Date) {
        _date.value = date
    }
    
    suspend fun saveChecklist(): Long? {
        return try {
            _isSaving.value = true
            _saveError.value = null
            
            val checklist = CarChecklist(
                date = _date.value,
                carInfo = _carInfo.value.trim(),
                vin = _vin.value.trim()
            )
            
            val checklistId = repository.insertChecklist(checklist)
            _isSaving.value = false
            checklistId
        } catch (e: Exception) {
            _isSaving.value = false
            _saveError.value = e.message ?: "Failed to save checklist"
            null
        }
    }
    
    fun clearError() {
        _saveError.value = null
    }
}

/**
 * Factory for creating NewChecklistViewModel
 */
class NewChecklistViewModelFactory(
    private val repository: ChecklistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewChecklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewChecklistViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

