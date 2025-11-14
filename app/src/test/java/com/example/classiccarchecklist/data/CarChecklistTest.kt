package com.example.classiccarchecklist.data

import org.junit.Test
import org.junit.Assert.*
import java.util.Date

/**
 * Unit tests for CarChecklist data model
 */
class CarChecklistTest {
    
    @Test
    fun carChecklist_hasDefaultValues() {
        val date = Date()
        val checklist = CarChecklist(date = date)
        
        assertEquals("Default id should be 0", 0, checklist.id)
        assertEquals("Date should match", date, checklist.date)
        assertEquals("Default carInfo should be empty", "", checklist.carInfo)
        assertEquals("Default vin should be empty", "", checklist.vin)
        assertNotNull("lastModified should be set", checklist.lastModified)
    }
    
    @Test
    fun carChecklist_canBeCreatedWithAllFields() {
        val date = Date()
        val lastModified = Date()
        val checklist = CarChecklist(
            id = 1,
            date = date,
            carInfo = "1965 Mustang",
            vin = "5F08F123456789",
            lastModified = lastModified
        )
        
        assertEquals(1, checklist.id)
        assertEquals(date, checklist.date)
        assertEquals("1965 Mustang", checklist.carInfo)
        assertEquals("5F08F123456789", checklist.vin)
        assertEquals(lastModified, checklist.lastModified)
    }
    
    @Test
    fun carChecklist_isDataClass() {
        val date = Date()
        val checklist1 = CarChecklist(id = 1, date = date, carInfo = "Test Car")
        val checklist2 = CarChecklist(id = 1, date = date, carInfo = "Test Car")
        
        assertEquals("Data classes with same values should be equal", checklist1, checklist2)
        assertEquals("Data classes with same values should have same hash", 
            checklist1.hashCode(), checklist2.hashCode())
    }
}

