package com.example.classiccarchecklist.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a single checklist item
 * 
 * @param id Unique identifier for the item (auto-generated)
 * @param checklistId Foreign key to the parent CarChecklist
 * @param section The section this item belongs to
 * @param question The question text for this item
 * @param type The type of input required (YES_NO, MULTI_CHOICE, TEXT_INPUT)
 * @param options For MULTI_CHOICE items, the list of available options (stored as JSON)
 * @param value The current value/answer for this item (null if not answered)
 */
@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = CarChecklist::class,
            parentColumns = ["id"],
            childColumns = ["checklistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["checklistId"])]
)
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val checklistId: Long,
    val section: String, // Stored as string, converted from ChecklistSection enum
    val question: String,
    val type: String, // Stored as string, converted from ChecklistItemType enum
    val options: String = "[]", // Stored as JSON string
    val value: String? = null
)

