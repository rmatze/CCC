package com.example.classiccarchecklist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entity representing a complete car checklist
 * 
 * @param id Unique identifier (auto-generated)
 * @param date Date when the checklist was created
 * @param carInfo Open text field for car information
 * @param vin VIN number of the car
 * @param lastModified Timestamp of last modification
 */
@Entity(tableName = "car_checklists")
data class CarChecklist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val carInfo: String = "",
    val vin: String = "",
    val lastModified: Date = Date()
)

