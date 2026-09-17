package com.example.campusmate
import android.content.Intent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.campusmate.data.AppDatabase
import com.example.campusmate.data.Assignment
import kotlinx.coroutines.launch

class AssignmentsActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assignments)

        val etSubject = findViewById<EditText>(R.id.etSubject)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etDueDate = findViewById<EditText>(R.id.etDueDate)

        val btnAddAssignment =
            findViewById<Button>(R.id.btnAddAssignment)

        val tvAssignments =
            findViewById<TextView>(R.id.tvAssignments)
        val btnShareAssignment =
            findViewById<Button>(R.id.btnShareAssignment)
        btnShareAssignment.setOnClickListener {

            val shareText =
                "📚 CampusMate Assignment\n\n" +
                        "Subject: DBMS\n" +
                        "Assignment: ER Diagram\n" +
                        "Due Date: 20 September"

            val shareIntent = Intent(Intent.ACTION_SEND)

            shareIntent.type = "text/plain"

            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                shareText
            )

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Assignment"
                )
            )
        }


        database = AppDatabase.getDatabase(this)

        fun loadAssignments() {

            lifecycleScope.launch {

                val assignments =
                    database.assignmentDao().getAllAssignments()

                if (assignments.isEmpty()) {

                    tvAssignments.text =
                        "Your Assignments\n\nNo assignments yet."

                } else {

                    val result =
                        StringBuilder("Your Assignments\n\n")

                    for (assignment in assignments) {

                        result.append("📚 ")
                        result.append(assignment.subject)
                        result.append("\n")

                        result.append("📝 ")
                        result.append(assignment.title)
                        result.append("\n")

                        result.append("📅 Due: ")
                        result.append(assignment.dueDate)
                        result.append("\n\n")
                    }

                    tvAssignments.text = result.toString()
                }
            }
        }

        loadAssignments()

        btnAddAssignment.setOnClickListener {

            val subject = etSubject.text.toString().trim()
            val title = etTitle.text.toString().trim()
            val dueDate = etDueDate.text.toString().trim()

            if (subject.isEmpty() ||
                title.isEmpty() ||
                dueDate.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                lifecycleScope.launch {

                    val assignment = Assignment(
                        subject = subject,
                        title = title,
                        dueDate = dueDate
                    )

                    database.assignmentDao()
                        .insertAssignment(assignment)

                    etSubject.text.clear()
                    etTitle.text.clear()
                    etDueDate.text.clear()

                    Toast.makeText(
                        this@AssignmentsActivity,
                        "Assignment added!",
                        Toast.LENGTH_SHORT
                    ).show()

                    loadAssignments()
                }
            }
        }
    }
}