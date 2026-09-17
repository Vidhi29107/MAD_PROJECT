package com.example.campusmate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subject: String,
    val attended: Int,
    val total: Int
)