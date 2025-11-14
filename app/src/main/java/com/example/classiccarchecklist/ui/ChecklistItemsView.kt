package com.example.classiccarchecklist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.classiccarchecklist.data.ChecklistItemDomain
import com.example.classiccarchecklist.data.ChecklistSection

/**
 * Composable that displays all checklist items grouped by section
 * Uses Column for use within a scrollable parent
 */
@Composable
fun ChecklistItemsView(
    items: List<ChecklistItemDomain>,
    onItemValueChanged: (Long, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    // Group items by section
    val itemsBySection = items.groupBy { it.section }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Display each section
        ChecklistSection.values().forEach { section ->
            val sectionItems = itemsBySection[section] ?: emptyList()
            if (sectionItems.isNotEmpty()) {
                SectionHeader(section = section)
                
                sectionItems.forEach { item ->
                    ChecklistItemRow(
                        item = item,
                        onValueChanged = { value ->
                            onItemValueChanged(item.id, value)
                        }
                    )
                }
                
                // Add spacing after section
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Section header composable following Material Design 3 guidelines
 */
@Composable
fun SectionHeader(
    section: ChecklistSection,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 1.dp
    ) {
        Text(
            text = section.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

/**
 * Individual checklist item row
 * Displays the question and appropriate widget based on item type
 */
@Composable
fun ChecklistItemRow(
    item: ChecklistItemDomain,
    onValueChanged: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = item.value != null
    val cardColor = if (isCompleted) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCompleted) 2.dp else 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Question text
            Text(
                text = item.question,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isCompleted) {
                    androidx.compose.ui.text.font.FontWeight.Medium
                } else {
                    androidx.compose.ui.text.font.FontWeight.Normal
                }
            )
            
            // Display appropriate widget based on item type
            when (item.type) {
                com.example.classiccarchecklist.data.ChecklistItemType.YES_NO -> {
                    YesNoItemWidget(
                        currentValue = item.value,
                        onValueChanged = onValueChanged,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                com.example.classiccarchecklist.data.ChecklistItemType.MULTI_CHOICE -> {
                    if (item.options.isNotEmpty()) {
                        MultiChoiceItemWidget(
                            options = item.options,
                            currentValue = item.value,
                            onValueChanged = onValueChanged,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        // Fallback if no options are defined
                        Text(
                            text = "No options available",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                com.example.classiccarchecklist.data.ChecklistItemType.TEXT_INPUT -> {
                    // Placeholder for Phase 7
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "TEXT INPUT (Phase 7)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

