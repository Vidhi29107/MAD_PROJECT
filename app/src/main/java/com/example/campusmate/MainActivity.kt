package com.example.campusmate

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.campusmate.data.AppDatabase
import com.example.campusmate.data.Exam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var tvNextExam: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        database = AppDatabase.getDatabase(this)

        // Dashboard cards
        val cardTimetable = findViewById<androidx.cardview.widget.CardView>(
            R.id.cardTimetable
        )

        val cardAssignments = findViewById<androidx.cardview.widget.CardView>(
            R.id.cardAssignments
        )

        val cardAttendance = findViewById<androidx.cardview.widget.CardView>(
            R.id.cardAttendance
        )

        val cardNotes = findViewById<androidx.cardview.widget.CardView>(
            R.id.cardNotes
        )

        val cardExam = findViewById<androidx.cardview.widget.CardView>(
            R.id.cardExam
        )

        // Next exam text
        tvNextExam = findViewById(R.id.tvNextExam)

        // Open Timetable
        cardTimetable.setOnClickListener {
            startActivity(
                Intent(this, TimetableActivity::class.java)
            )
        }

        // Open Assignments
        cardAssignments.setOnClickListener {
            startActivity(
                Intent(this, AssignmentsActivity::class.java)
            )
        }

        // Open Attendance
        cardAttendance.setOnClickListener {
            startActivity(
                Intent(this, AttendanceActivity::class.java)
            )
        }

        // Open Notes
        cardNotes.setOnClickListener {
            startActivity(
                Intent(this, NotesActivity::class.java)
            )
        }

        // Open Exams
        cardExam.setOnClickListener {
            startActivity(
                Intent(this, ExamActivity::class.java)
            )
        }

        // Load nearest upcoming exam
        loadNextExam()
    }

    override fun onResume() {
        super.onResume()

        // Refresh dashboard whenever returning to it
        if (::database.isInitialized) {
            loadNextExam()
        }
    }

    private fun loadNextExam() {

        CoroutineScope(Dispatchers.IO).launch {

            val exams = database.examDao().getAllExams()

            val nextExam = findNearestUpcomingExam(exams)

            withContext(Dispatchers.Main) {

                if (nextExam == null) {

                    tvNextExam.text =
                        "No upcoming exams"

                } else {

                    val countdown =
                        calculateDaysRemaining(nextExam.date)

                    tvNextExam.text =
                        "${nextExam.subject}\n" +
                                "📅 ${nextExam.date}\n" +
                                "⏳ $countdown"
                }
            }
        }
    }

    private fun findNearestUpcomingExam(
        exams: List<Exam>
    ): Exam? {

        val dateFormat =
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )

        dateFormat.isLenient = false

        val today = Calendar.getInstance()

        setStartOfDay(today)

        return exams
            .mapNotNull { exam ->

                try {

                    val date =
                        dateFormat.parse(exam.date)

                    if (date != null) {

                        val examCalendar =
                            Calendar.getInstance()

                        examCalendar.time = date

                        setStartOfDay(examCalendar)

                        if (!examCalendar.before(today)) {

                            Pair(exam, examCalendar.time)

                        } else {

                            null
                        }

                    } else {

                        null
                    }

                } catch (e: Exception) {

                    null
                }
            }
            .minByOrNull {
                it.second.time
            }
            ?.first
    }

    private fun calculateDaysRemaining(
        dateString: String
    ): String {

        return try {

            val dateFormat =
                SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

            dateFormat.isLenient = false

            val examDate =
                dateFormat.parse(dateString)
                    ?: return "Invalid date"

            val examCalendar =
                Calendar.getInstance()

            examCalendar.time = examDate

            val today =
                Calendar.getInstance()

            setStartOfDay(examCalendar)
            setStartOfDay(today)

            val difference =
                examCalendar.timeInMillis -
                        today.timeInMillis

            val days =
                difference /
                        (24 * 60 * 60 * 1000)

            when {

                days > 1 ->
                    "$days days left"

                days == 1L ->
                    "1 day left"

                days == 0L ->
                    "Exam is today!"

                else ->
                    "Exam passed"
            }

        } catch (e: Exception) {

            "Invalid date"
        }
    }

    private fun setStartOfDay(
        calendar: Calendar
    ) {

        calendar.set(
            Calendar.HOUR_OF_DAY,
            0
        )

        calendar.set(
            Calendar.MINUTE,
            0
        )

        calendar.set(
            Calendar.SECOND,
            0
        )

        calendar.set(
            Calendar.MILLISECOND,
            0
        )
    }
}