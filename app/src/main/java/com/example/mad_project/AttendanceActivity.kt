package com.example.mad_project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AttendanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect Kotlin with the Attendance XML layout
        setContentView(R.layout.activity_attendance)

        // Find the UI components
        val etAttended = findViewById<EditText>(R.id.etAttended)
        val etTotal = findViewById<EditText>(R.id.etTotal)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // When Calculate button is clicked
        btnCalculate.setOnClickListener {

            // Get values entered by the user
            val attendedText = etAttended.text.toString()
            val totalText = etTotal.text.toString()

            // Check whether fields are empty
            if (attendedText.isEmpty() || totalText.isEmpty()) {

                Toast.makeText(
                    this,
                    "Please enter both values",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Convert text into numbers
            val attended = attendedText.toInt()
            val total = totalText.toInt()

            // Check whether total classes are valid
            if (total <= 0) {

                Toast.makeText(
                    this,
                    "Total classes must be greater than 0",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Check whether attended classes are valid
            if (attended < 0 || attended > total) {

                Toast.makeText(
                    this,
                    "Attended classes must be between 0 and total classes",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Calculate attendance percentage
            val attendance = (attended.toDouble() / total.toDouble()) * 100

            // Display result
            tvResult.text = "Attendance: %.2f%%".format(attendance)
        }
    }
}