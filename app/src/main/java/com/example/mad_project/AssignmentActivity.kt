package com.example.mad_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AssignmentActivity : AppCompatActivity() {

    private val assignments = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_assignment)

        val etAssignmentTitle =
            findViewById<EditText>(R.id.etAssignmentTitle)

        val etSubject =
            findViewById<EditText>(R.id.etSubject)

        val etDueDate =
            findViewById<EditText>(R.id.etDueDate)

        val btnAddAssignment =
            findViewById<Button>(R.id.btnAddAssignment)

        val btnShare =
            findViewById<Button>(R.id.btnShare)

        val tvAssignments =
            findViewById<TextView>(R.id.tvAssignments)


        // Add Assignment
        btnAddAssignment.setOnClickListener {

            val title = etAssignmentTitle.text.toString().trim()
            val subject = etSubject.text.toString().trim()
            val dueDate = etDueDate.text.toString().trim()

            if (title.isEmpty() ||
                subject.isEmpty() ||
                dueDate.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val assignment =
                "Title: $title\n" +
                        "Subject: $subject\n" +
                        "Due Date: $dueDate"

            assignments.add(assignment)

            tvAssignments.text =
                assignments.joinToString("\n\n")

            etAssignmentTitle.text.clear()
            etSubject.text.clear()
            etDueDate.text.clear()

            Toast.makeText(
                this,
                "Assignment added successfully",
                Toast.LENGTH_SHORT
            ).show()
        }


        // Share Assignments
        btnShare.setOnClickListener {

            if (assignments.isEmpty()) {

                Toast.makeText(
                    this,
                    "No assignments to share",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val shareText =
                assignments.joinToString("\n\n")

            val shareIntent = Intent(Intent.ACTION_SEND)

            shareIntent.type = "text/plain"

            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                shareText
            )

            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Assignments"
                )
            )
        }
    }
}