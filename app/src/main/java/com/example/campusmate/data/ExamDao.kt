package com.example.campusmate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExamDao {

    @Insert
    suspend fun insertExam(exam: Exam)

    @Query("SELECT * FROM exams ORDER BY date ASC")
    suspend fun getAllExams(): List<Exam>

    @Delete
    suspend fun deleteExam(exam: Exam)

    @Query("DELETE FROM exams")
    suspend fun deleteAllExams()
}