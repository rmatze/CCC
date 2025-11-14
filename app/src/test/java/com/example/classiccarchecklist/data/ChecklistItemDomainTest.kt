package com.example.classiccarchecklist.data

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ChecklistItemDomain data model
 */
class ChecklistItemDomainTest {
    
    @Test
    fun checklistItemDomain_hasDefaultValues() {
        val item = ChecklistItemDomain(
            section = ChecklistSection.PAINT_BODY,
            question = "Test question",
            type = ChecklistItemType.YES_NO
        )
        
        assertEquals("Default id should be 0", 0, item.id)
        assertEquals(ChecklistSection.PAINT_BODY, item.section)
        assertEquals("Test question", item.question)
        assertEquals(ChecklistItemType.YES_NO, item.type)
        assertTrue("Default options should be empty", item.options.isEmpty())
        assertNull("Default value should be null", item.value)
    }
    
    @Test
    fun checklistItemDomain_canBeCreatedWithAllFields() {
        val options = listOf("Option 1", "Option 2", "Option 3")
        val item = ChecklistItemDomain(
            id = 1,
            section = ChecklistSection.ENGINE_BAY,
            question = "What is the condition?",
            type = ChecklistItemType.MULTI_CHOICE,
            options = options,
            value = "Option 2"
        )
        
        assertEquals(1, item.id)
        assertEquals(ChecklistSection.ENGINE_BAY, item.section)
        assertEquals("What is the condition?", item.question)
        assertEquals(ChecklistItemType.MULTI_CHOICE, item.type)
        assertEquals(options, item.options)
        assertEquals("Option 2", item.value)
    }
    
    @Test
    fun checklistItemDomain_isDataClass() {
        val item1 = ChecklistItemDomain(
            id = 1,
            section = ChecklistSection.PAINT_BODY,
            question = "Test",
            type = ChecklistItemType.YES_NO
        )
        val item2 = ChecklistItemDomain(
            id = 1,
            section = ChecklistSection.PAINT_BODY,
            question = "Test",
            type = ChecklistItemType.YES_NO
        )
        
        assertEquals("Data classes with same values should be equal", item1, item2)
    }
}

