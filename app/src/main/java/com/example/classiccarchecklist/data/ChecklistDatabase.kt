package com.example.classiccarchecklist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Room database for the Classic Car Checklist app
 */
@Database(
    entities = [CarChecklist::class, ChecklistItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChecklistDatabase : RoomDatabase() {
    
    abstract fun carChecklistDao(): CarChecklistDao
    abstract fun checklistItemDao(): ChecklistItemDao
    
    companion object {
        @Volatile
        private var INSTANCE: ChecklistDatabase? = null
        
        fun getDatabase(context: Context): ChecklistDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChecklistDatabase::class.java,
                    "checklist_database"
                )
                    .fallbackToDestructiveMigration() // For development - remove in production
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

