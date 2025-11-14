package com.example.classiccarchecklist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for ChecklistItem operations
 */
@Dao
interface ChecklistItemDao {
    
    @Query("SELECT * FROM checklist_items WHERE checklistId = :checklistId ORDER BY id")
    fun getItemsByChecklistId(checklistId: Long): Flow<List<ChecklistItem>>
    
    @Query("SELECT * FROM checklist_items WHERE checklistId = :checklistId ORDER BY id")
    suspend fun getItemsByChecklistIdSuspend(checklistId: Long): List<ChecklistItem>
    
    @Query("SELECT * FROM checklist_items WHERE id = :id")
    suspend fun getItemById(id: Long): ChecklistItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChecklistItem): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<ChecklistItem>)
    
    @Update
    suspend fun updateItem(item: ChecklistItem)
    
    @Delete
    suspend fun deleteItem(item: ChecklistItem)
    
    @Query("DELETE FROM checklist_items WHERE checklistId = :checklistId")
    suspend fun deleteItemsByChecklistId(checklistId: Long)
}

