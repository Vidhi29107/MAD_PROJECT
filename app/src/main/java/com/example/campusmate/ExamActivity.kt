package com.example.campusmate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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

class ExamActivity : AppCompatActivity() {

    private lateinit var etExamSubject: EditText
    private lateinit var etExamDate: EditText
    private lateinit var btnCalculateExam: Button
    private lateinit var btnClearExams: Button
    private lateinit var tvCountdown: TextView

    private lateinit var database: AppDatabase

    private val dateFormat =
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

        etExamSubject = findViewById(R.id.etExamSubject)
        etExamDate = findViewById(R.id.etExamDate)
        btnCalculateExam = findViewById(R.id.btnCalculateExam)
        btnClearExams = findViewById(R.id.btnClearExams)
        tvCountdown = findViewById(R.id.tvCountdown)

        database = AppDatabase.getDatabase(this)

        displayExams()

        btnCalculateExam.setOnClickListener {

            val subject = etExamSubject.text.toString().trim()
            val date = etExamDate.text.toString().trim()

            if (subject.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter exam subject",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!isValidDate(date)) {
                Toast.makeText(
                    this,
                    "Enter date as DD/MM/YYYY",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val exam = Exam(
                subject = subject,
                date = date
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.examDao().insertExam(exam)

                withContext(Dispatchers.Main) {

                    etExamSubject.text.clear()
                    etExamDate.text.clear()

                    displayExams()

                    Toast.makeText(
                        this@ExamActivity,
                        "Exam saved to database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        btnClearExams.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {

                database.examDao().deleteAllExams()

                withContext(Dispatchers.Main) {

                    tvCountdown.text = "No exams saved yet"

                    Toast.makeText(
                        this@ExamActivity,
                        "All exams cleared",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun displayExams() {

        CoroutineScope(Dispatchers.IO).launch {

            val exams = database.examDao().getAllExams()

            withContext(Dispatchers.Main) {

                if (exams.isEmpty()) {
                    tvCountdown.text = "No exams saved yet"
                    return@withContext
                }

                val result = StringBuilder()

                for (exam in exams) {

                    result.append("📚 ")
                        .append(exam.subject)
                        .append("\n")

                    result.append("📅 ")
                        .append(exam.date)
                        .append("\n")

                    result.append(
                        calculateCountdown(exam.date)
                    )

                    result.append("\n\n")
                }

                tvCountdown.text = result.toString().trim()
            }
        }
    }

    private fun calculateCountdown(dateString: String): String {

        return try {

            dateFormat.isLenient = false

            val examDate = dateFormat.parse(dateString)
                ?: return "⚠️ Invalid date"

            val examCalendar = Calendar.getInstance()
            examCalendar.time = examDate
            setStartOfDay(examCalendar)

            val today = Calendar.getInstance()
            setStartOfDay(today)

            val difference =
                examCalendar.timeInMillis -
                        today.timeInMillis

            val days =
                difference / (24 * 60 * 60 * 1000)

            when {

                days > 1 ->
                    "⏳ $days days left"

                days == 1L ->
                    "⏳ 1 day left"

                days == 0L ->
                    "🎯 Exam is today!"

                else ->
                    "⚠️ Exam date has passed"
            }

        } catch (e: Exception) {
            "⚠️ Invalid date"
        }
    }

    private fun isValidDate(date: String): Boolean {

        return try {
            dateFormat.isLenient = false
            dateFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun setStartOfDay(calendar: Calendar) {

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
    }
}