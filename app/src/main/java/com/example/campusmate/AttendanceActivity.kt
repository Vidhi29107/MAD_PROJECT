package com.example.campusmate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusmate.data.AppDatabase
import com.example.campusmate.data.Attendance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AttendanceActivity : AppCompatActivity() {

    private lateinit var etSubject: EditText
    private lateinit var etAttended: EditText
    private lateinit var etTotal: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvResult: TextView

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        etSubject = findViewById(R.id.etSubject)
        etAttended = findViewById(R.id.etAttended)
        etTotal = findViewById(R.id.etTotal)
        btnCalculate = findViewById(R.id.btnCalculate)
        tvResult = findViewById(R.id.tvResult)

        database = AppDatabase.getDatabase(this)

        loadAttendance()

        btnCalculate.setOnClickListener {

            val subject = etSubject.text.toString().trim()
            val attendedText = etAttended.text.toString().trim()
            val totalText = etTotal.text.toString().trim()

            if (subject.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter subject name",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val attended = attendedText.toIntOrNull()
            val total = totalText.toIntOrNull()

            if (attended == null || total == null) {
                Toast.makeText(
                    this,
                    "Please enter valid class numbers",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (total <= 0) {
                Toast.makeText(
                    this,
                    "Total classes must be greater than 0",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (attended < 0 || attended > total) {
                Toast.makeText(
                    this,
                    "Attended classes must be between 0 and total",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val record = Attendance(
                subject = subject,
                attended = attended,
                total = total
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.attendanceDao().insertAttendance(record)

                withContext(Dispatchers.Main) {

                    etSubject.text.clear()
                    etAttended.text.clear()
                    etTotal.text.clear()

                    loadAttendance()

                    Toast.makeText(
                        this@AttendanceActivity,
                        "Attendance saved to database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadAttendance() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = database.attendanceDao().getAllAttendance()

            withContext(Dispatchers.Main) {

                if (list.isEmpty()) {
                    tvResult.text = "No attendance records saved yet"
                    return@withContext
                }

                val result = StringBuilder()

                var totalAttended = 0
                var totalClasses = 0

                for (record in list) {

                    val percentage =
                        record.attended.toDouble() /
                                record.total.toDouble() * 100

                    totalAttended += record.attended
                    totalClasses += record.total

                    result.append("📚 ")
                        .append(record.subject)
                        .append("\n")

                    result.append("Classes: ")
                        .append(record.attended)
                        .append("/")
                        .append(record.total)
                        .append("\n")

                    result.append(
                        String.format(
                            Locale.getDefault(),
                            "Attendance: %.2f%%",
                            percentage
                        )
                    )

                    if (percentage < 75) {
                        result.append("\n⚠️ Below 75%")
                    } else {
                        result.append("\n✅ 75% or above")
                    }

                    result.append("\n\n")
                }

                if (totalClasses > 0) {

                    val overall =
                        totalAttended.toDouble() /
                                totalClasses.toDouble() * 100

                    result.append("━━━━━━━━━━━━━━━━\n")

                    result.append(
                        String.format(
                            Locale.getDefault(),
                            "📊 Overall Attendance: %.2f%%",
                            overall
                        )
                    )
                }

                tvResult.text = result.toString().trim()
            }
        }
    }
}