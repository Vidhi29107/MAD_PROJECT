package com.example.campusmate.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val subject: String,

    val title: String,

    val dueDate: String
)