package com.example.campusmate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable")
data class Timetable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val day: String,
    val time: String,
    val subject: String,
    val room: String
)