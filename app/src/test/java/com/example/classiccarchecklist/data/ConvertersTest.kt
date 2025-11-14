package com.example.classiccarchecklist.data

import org.junit.Test
import org.junit.Assert.*
import java.util.Date

/**
 * Unit tests for Room type converters
 */
class ConvertersTest {
    
    private val converters = Converters()
    
    @Test
    fun dateConverter_roundTrip() {
        val originalDate = Date()
        val timestamp = converters.dateToTimestamp(originalDate)
        assertNotNull("Timestamp should not be null", timestamp)
        
        val convertedDate = converters.fromTimestamp(timestamp)
        assertNotNull("Converted date should not be null", convertedDate)
        assertEquals("Date should match after round trip", originalDate.time, convertedDate?.time)
    }
    
    @Test
    fun dateConverter_nullHandling() {
        assertNull("Null date should return null timestamp", converters.dateToTimestamp(null))
        assertNull("Null timestamp should return null date", converters.fromTimestamp(null))
    }
    
    @Test
    fun checklistSectionConverter_roundTrip() {
        ChecklistSection.values().forEach { section ->
            val stringValue = converters.checklistSectionToString(section)
            val convertedSection = converters.fromChecklistSection(stringValue)
            assertEquals("Section should match after round trip", section, convertedSection)
        }
    }
    
    @Test
    fun checklistItemTypeConverter_roundTrip() {
        ChecklistItemType.values().forEach { type ->
            val stringValue = converters.checklistItemTypeToString(type)
            val convertedType = converters.fromChecklistItemType(stringValue)
            assertEquals("Type should match after round trip", type, convertedType)
        }
    }
    
    @Test
    fun stringListConverter_roundTrip() {
        val originalList = listOf("Option 1", "Option 2", "Option 3")
        val jsonString = converters.stringListToString(originalList)
        assertNotNull("JSON string should not be null", jsonString)
        assertTrue("JSON string should not be empty", jsonString.isNotEmpty())
        
        val convertedList = converters.fromStringList(jsonString)
        assertEquals("List size should match", originalList.size, convertedList.size)
        assertEquals("List content should match", originalList, convertedList)
    }
    
    @Test
    fun stringListConverter_emptyList() {
        val emptyList = emptyList<String>()
        val jsonString = converters.stringListToString(emptyList)
        val convertedList = converters.fromStringList(jsonString)
        assertTrue("Empty list should remain empty", convertedList.isEmpty())
    }
    
    @Test
    fun stringListConverter_specialCharacters() {
        val listWithSpecialChars = listOf("Option 1", "Option & 2", "Option \"3\"", "Option '4'")
        val jsonString = converters.stringListToString(listWithSpecialChars)
        val convertedList = converters.fromStringList(jsonString)
        assertEquals("List with special characters should match", listWithSpecialChars, convertedList)
    }
}

