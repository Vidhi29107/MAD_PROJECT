package com.example.mad_project

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val cardTimetable =
            findViewById<CardView>(R.id.cardTimetable)

        cardTimetable.setOnClickListener {

            val intent =
                Intent(this, TimetableActivity::class.java)

            startActivity(intent)
        }
        val cardAttendance =
            findViewById<CardView>(R.id.cardAttendance)

        cardAttendance.setOnClickListener {

            val intent =
                Intent(this, AttendanceActivity::class.java)

            startActivity(intent)
        }
        // Assignment Card
        val cardAssignments =
            findViewById<CardView>(R.id.cardAssignments)

        cardAssignments.setOnClickListener {

            val intent =
                Intent(this, AssignmentActivity::class.java)

            startActivity(intent)
        }
    }
}