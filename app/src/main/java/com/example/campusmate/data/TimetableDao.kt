package com.example.campusmate.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimetableDao {

    @Insert
    suspend fun insertTimetable(timetable: Timetable)

    @Query("""
        SELECT * FROM timetable
        ORDER BY
        CASE day
            WHEN 'Monday' THEN 1
            WHEN 'Tuesday' THEN 2
            WHEN 'Wednesday' THEN 3
            WHEN 'Thursday' THEN 4
            WHEN 'Friday' THEN 5
            WHEN 'Saturday' THEN 6
            WHEN 'Sunday' THEN 7
            ELSE 8
        END,
        time ASC
    """)
    suspend fun getAllTimetable(): List<Timetable>

    @Delete
    suspend fun deleteTimetable(timetable: Timetable)

    @Query("DELETE FROM timetable")
    suspend fun deleteAllTimetable()
}