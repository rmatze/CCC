package com.example.classiccarchecklist.data

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for ChecklistSection enum
 */
class ChecklistSectionTest {
    
    @Test
    fun allSectionsHaveDisplayNames() {
        ChecklistSection.values().forEach { section ->
            assertNotNull("Section ${section.name} should have a display name", section.displayName)
            assertTrue("Section ${section.name} display name should not be empty", 
                section.displayName.isNotEmpty())
        }
    }
    
    @Test
    fun sectionCountMatchesExpected() {
        // Verify we have all 11 sections from the checklist
        assertEquals(11, ChecklistSection.values().size)
    }
    
    @Test
    fun sectionNamesAreUnique() {
        val names = ChecklistSection.values().map { it.name }
        assertEquals("All section names should be unique", 
            names.size, names.distinct().size)
    }
}

