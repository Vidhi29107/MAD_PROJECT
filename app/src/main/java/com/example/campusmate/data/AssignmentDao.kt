package com.example.campusmate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AssignmentDao {

    @Insert
    suspend fun insertAssignment(assignment: Assignment)

    @Query("SELECT * FROM assignments ORDER BY id DESC")
    suspend fun getAllAssignments(): List<Assignment>
}