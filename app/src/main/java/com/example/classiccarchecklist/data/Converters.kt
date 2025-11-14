package com.example.classiccarchecklist.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

/**
 * Type converters for Room database to handle custom types
 */
class Converters {
    private val gson = Gson()

    // Date converters
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    // ChecklistSection converter
    @TypeConverter
    fun fromChecklistSection(value: String): ChecklistSection {
        return ChecklistSection.valueOf(value)
    }

    @TypeConverter
    fun checklistSectionToString(section: ChecklistSection): String {
        return section.name
    }

    // ChecklistItemType converter
    @TypeConverter
    fun fromChecklistItemType(value: String): ChecklistItemType {
        return ChecklistItemType.valueOf(value)
    }

    @TypeConverter
    fun checklistItemTypeToString(type: ChecklistItemType): String {
        return type.name
    }

    // List<String> converter for options
    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun stringListToString(list: List<String>): String {
        return gson.toJson(list)
    }
}

