package com.example.campusmate.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        Note::class,
        Assignment::class,
        Attendance::class,
        Exam::class,
        Timetable::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun noteDao(): NoteDao

    abstract fun assignmentDao(): AssignmentDao

    abstract fun attendanceDao(): AttendanceDao

    abstract fun examDao(): ExamDao

    abstract fun timetableDao(): TimetableDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null


        // Version 2 → 3
        // Adds Attendance table
        private val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS attendance (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        subject TEXT NOT NULL,
                        attended INTEGER NOT NULL,
                        total INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }


        // Version 3 → 4
        // Adds Exams and Timetable tables
        private val MIGRATION_3_4 = object : Migration(3, 4) {

            override fun migrate(database: SupportSQLiteDatabase) {

                // Exams table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS exams (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        subject TEXT NOT NULL,
                        date TEXT NOT NULL
                    )
                    """.trimIndent()
                )


                // Timetable table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS timetable (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        day TEXT NOT NULL,
                        time TEXT NOT NULL,
                        subject TEXT NOT NULL,
                        room TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }


        // Version 4 → 5
        // Updates Notes table to store:
        // Subject + Note Content
        private val MIGRATION_4_5 = object : Migration(4, 5) {

            override fun migrate(database: SupportSQLiteDatabase) {

                // Remove old notes table
                database.execSQL(
                    "DROP TABLE IF EXISTS notes"
                )


                // Create new notes table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS notes (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        subject TEXT NOT NULL,
                        content TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }


        // Get database instance
        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campusmate_database"
                )
                    .addMigrations(
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5
                    )
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}