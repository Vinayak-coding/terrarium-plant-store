package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReminderType(val displayName: String, val defaultIntervalDays: Int, val iconName: String) {
    WATERING("Watering", 7, "water_drop"),
    FERTILIZING("Fertilizing", 30, "science"),
    REPOTTING("Repotting", 180, "nest_cam_wired_stand"),
    PRUNING("Pruning & Trimming", 45, "content_cut"),
    ROTATING("Rotating Plant", 14, "refresh")
}

@Entity(tableName = "care_reminders")
data class CareReminder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val plantName: String,
    val plantType: String,
    val reminderType: ReminderType,
    val frequencyDays: Int,
    val nextDueDateMillis: Long,
    val isCompleted: Boolean = false,
    val notes: String = ""
)
