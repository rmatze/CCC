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
    
    // Map of checklist ID to (completed, total) pair
    private val _completionStats = MutableStateFlow<Map<Long, Pair<Int, Int>>>(emptyMap())
    val completionStats: StateFlow<Map<Long, Pair<Int, Int>>> = _completionStats.asStateFlow()
    
    init {
        loadChecklists()
    }
    
    private fun loadCompletionStats() {
        viewModelScope.launch {
            try {
                val statsMap = mutableMapOf<Long, Pair<Int, Int>>()
                _checklists.value.forEach { checklist ->
                    try {
                        val stats = repository.getCompletionStats(checklist.id)
                        statsMap[checklist.id] = stats
                    } catch (e: Exception) {
                        // If stats fail to load for one checklist, use default
                        statsMap[checklist.id] = Pair(0, 0)
                    }
                }
                _completionStats.value = statsMap
            } catch (e: Exception) {
                // If stats loading fails completely, use empty map
                _completionStats.value = emptyMap()
            }
        }
    }
    
    private fun loadChecklists() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllChecklists().collect { list ->
                _checklists.value = list
                _isLoading.value = false
                // Load completion stats when checklists update
                loadCompletionStats()
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

