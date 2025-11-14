package com.example.classiccarchecklist.data

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ChecklistItemType enum
 */
class ChecklistItemTypeTest {
    
    @Test
    fun allTypesAreDefined() {
        val expectedTypes = listOf("YES_NO", "MULTI_CHOICE", "TEXT_INPUT")
        val actualTypes = ChecklistItemType.values().map { it.name }
        
        expectedTypes.forEach { type ->
            assertTrue("Type $type should be defined", actualTypes.contains(type))
        }
    }
    
    @Test
    fun typeCountMatchesExpected() {
        assertEquals(3, ChecklistItemType.values().size)
    }
}

