package com.example.classiccarchecklist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for CarChecklist operations
 */
@Dao
interface CarChecklistDao {
    
    @Query("SELECT * FROM car_checklists ORDER BY lastModified DESC")
    fun getAllChecklists(): Flow<List<CarChecklist>>
    
    @Query("SELECT * FROM car_checklists WHERE id = :id")
    suspend fun getChecklistById(id: Long): CarChecklist?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklist(checklist: CarChecklist): Long
    
    @Update
    suspend fun updateChecklist(checklist: CarChecklist)
    
    @Delete
    suspend fun deleteChecklist(checklist: CarChecklist)
    
    @Query("DELETE FROM car_checklists WHERE id = :id")
    suspend fun deleteChecklistById(id: Long)
}

