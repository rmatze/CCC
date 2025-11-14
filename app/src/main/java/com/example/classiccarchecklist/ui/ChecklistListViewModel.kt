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
 * ViewModel for the checklist list screen
 */
class ChecklistListViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {
    
    private val _checklists = MutableStateFlow<List<CarChecklist>>(emptyList())
    val checklists: StateFlow<List<CarChecklist>> = _checklists.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadChecklists()
    }
    
    private fun loadChecklists() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllChecklists().collect { list ->
                _checklists.value = list
                _isLoading.value = false
            }
        }
    }
    
    fun deleteChecklist(checklist: CarChecklist) {
        viewModelScope.launch {
            repository.deleteChecklist(checklist)
        }
    }
}

/**
 * Factory for creating ChecklistListViewModel
 */
class ChecklistListViewModelFactory(
    private val repository: ChecklistRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChecklistListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

