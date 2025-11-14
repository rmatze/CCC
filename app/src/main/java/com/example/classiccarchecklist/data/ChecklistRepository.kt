package com.example.classiccarchecklist.data

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

/**
 * Repository for managing checklist data
 * Handles conversion between database entities and domain models
 */
class ChecklistRepository(
    private val checklistDao: CarChecklistDao,
    private val itemDao: ChecklistItemDao
) {
    private val gson = Gson()
    
    // CarChecklist operations
    fun getAllChecklists(): Flow<List<CarChecklist>> = checklistDao.getAllChecklists()
    
    suspend fun getChecklistById(id: Long): CarChecklist? = checklistDao.getChecklistById(id)
    
    suspend fun insertChecklist(checklist: CarChecklist): Long {
        val updated = checklist.copy(lastModified = Date())
        return checklistDao.insertChecklist(updated)
    }
    
    suspend fun updateChecklist(checklist: CarChecklist) {
        val updated = checklist.copy(lastModified = Date())
        checklistDao.updateChecklist(updated)
    }
    
    suspend fun deleteChecklist(checklist: CarChecklist) {
        checklistDao.deleteChecklist(checklist)
        itemDao.deleteItemsByChecklistId(checklist.id)
    }
    
    suspend fun deleteChecklistById(id: Long) {
        checklistDao.deleteChecklistById(id)
        itemDao.deleteItemsByChecklistId(id)
    }
    
    // ChecklistItem operations
    fun getItemsByChecklistId(checklistId: Long): Flow<List<ChecklistItemDomain>> {
        return itemDao.getItemsByChecklistId(checklistId).map { items ->
            items.map { it.toDomain() }
        }
    }
    
    suspend fun getItemsByChecklistIdSuspend(checklistId: Long): List<ChecklistItemDomain> {
        return itemDao.getItemsByChecklistIdSuspend(checklistId).map { it.toDomain() }
    }
    
    suspend fun insertItem(item: ChecklistItemDomain, checklistId: Long): Long {
        return itemDao.insertItem(item.toEntity(checklistId))
    }
    
    suspend fun insertItems(items: List<ChecklistItemDomain>, checklistId: Long) {
        val entities = items.map { it.toEntity(checklistId) }
        itemDao.insertItems(entities)
    }
    
    suspend fun updateItem(item: ChecklistItemDomain, checklistId: Long) {
        itemDao.updateItem(item.toEntity(checklistId))
    }
    
    suspend fun deleteItem(item: ChecklistItemDomain, checklistId: Long) {
        itemDao.deleteItem(item.toEntity(checklistId))
    }
    
    /**
     * Get completion statistics for a checklist
     */
    suspend fun getCompletionStats(checklistId: Long): Pair<Int, Int> {
        val items = getItemsByChecklistIdSuspend(checklistId)
        val total = items.size
        val completed = items.count { it.value != null }
        return Pair(completed, total)
    }
    
    // Helper extension functions for conversion
    private fun ChecklistItem.toDomain(): ChecklistItemDomain {
        val optionsList = try {
            val listType = object : com.google.gson.reflect.TypeToken<List<String>>() {}.type
            gson.fromJson<List<String>>(options, listType) ?: emptyList<String>()
        } catch (e: Exception) {
            emptyList<String>()
        }
        
        return ChecklistItemDomain(
            id = id,
            section = ChecklistSection.valueOf(section),
            question = question,
            type = ChecklistItemType.valueOf(type),
            options = optionsList,
            value = value
        )
    }
    
    private fun ChecklistItemDomain.toEntity(checklistId: Long): ChecklistItem {
        return ChecklistItem(
            id = id,
            checklistId = checklistId,
            section = section.name,
            question = question,
            type = type.name,
            options = gson.toJson(options),
            value = value
        )
    }
}

/**
 * Domain model for ChecklistItem that uses enums
 */
data class ChecklistItemDomain(
    val id: Long = 0,
    val section: ChecklistSection,
    val question: String,
    val type: ChecklistItemType,
    val options: List<String> = emptyList(),
    val value: String? = null
)

