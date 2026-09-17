package com.example.campusmate

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campusmate.data.AppDatabase
import com.example.campusmate.data.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesActivity : AppCompatActivity() {

    private lateinit var etSubject: EditText
    private lateinit var etNote: EditText
    private lateinit var btnSaveNote: Button
    private lateinit var tvNotes: TextView

    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notes)

        etSubject = findViewById(R.id.etSubject)
        etNote = findViewById(R.id.etNote)
        btnSaveNote = findViewById(R.id.btnSaveNote)
        tvNotes = findViewById(R.id.tvNotes)

        database = AppDatabase.getDatabase(this)

        loadNotes()

        btnSaveNote.setOnClickListener {

            val subject = etSubject.text.toString().trim()
            val content = etNote.text.toString().trim()

            if (subject.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter subject",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (content.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter your notes",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val note = Note(
                subject = subject,
                content = content
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.noteDao().insertNote(note)

                withContext(Dispatchers.Main) {

                    etSubject.text.clear()
                    etNote.text.clear()

                    loadNotes()

                    Toast.makeText(
                        this@NotesActivity,
                        "Note saved successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadNotes() {

        CoroutineScope(Dispatchers.IO).launch {

            val notes = database.noteDao().getAllNotes()

            withContext(Dispatchers.Main) {

                if (notes.isEmpty()) {

                    tvNotes.text = "📝 No notes saved yet"

                    return@withContext
                }

                val result = StringBuilder()

                for (note in notes) {

                    result.append("📚 ")
                        .append(note.subject)
                        .append("\n")

                    result.append(note.content)
                        .append("\n")

                    result.append("────────────────────")
                        .append("\n\n")
                }

                tvNotes.text = result.toString().trim()
            }
        }
    }
}