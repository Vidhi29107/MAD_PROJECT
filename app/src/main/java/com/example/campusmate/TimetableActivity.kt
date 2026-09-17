package com.example.campusmate

import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.campusmate.data.AppDatabase
import com.example.campusmate.data.Timetable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimetableActivity : AppCompatActivity() {

    private lateinit var spinnerDay: Spinner
    private lateinit var etClassTime: EditText
    private lateinit var etClassSubject: EditText
    private lateinit var etClassRoom: EditText
    private lateinit var btnAddClass: Button
    private lateinit var timetableContainer: LinearLayout

    private lateinit var database: AppDatabase

    private val days = arrayOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        spinnerDay = findViewById(R.id.spinnerDay)
        etClassTime = findViewById(R.id.etClassTime)
        etClassSubject = findViewById(R.id.etClassSubject)
        etClassRoom = findViewById(R.id.etClassRoom)
        btnAddClass = findViewById(R.id.btnAddClass)
        timetableContainer = findViewById(R.id.timetableContainer)

        database = AppDatabase.getDatabase(this)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            days
        )

        spinnerDay.adapter = adapter

        loadTimetable()

        btnAddClass.setOnClickListener {

            val day = spinnerDay.selectedItem.toString()
            val time = etClassTime.text.toString().trim()
            val subject = etClassSubject.text.toString().trim()
            val room = etClassRoom.text.toString().trim()

            if (time.isEmpty() ||
                subject.isEmpty() ||
                room.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please fill all class details",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val timetable = Timetable(
                day = day,
                time = time,
                subject = subject,
                room = room
            )

            CoroutineScope(Dispatchers.IO).launch {

                database.timetableDao()
                    .insertTimetable(timetable)

                withContext(Dispatchers.Main) {

                    etClassTime.text.clear()
                    etClassSubject.text.clear()
                    etClassRoom.text.clear()

                    loadTimetable()

                    Toast.makeText(
                        this@TimetableActivity,
                        "Class saved to database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun loadTimetable() {

        CoroutineScope(Dispatchers.IO).launch {

            val list =
                database.timetableDao().getAllTimetable()

            withContext(Dispatchers.Main) {

                timetableContainer.removeAllViews()

                if (list.isEmpty()) {

                    val emptyText = TextView(this@TimetableActivity)

                    emptyText.text =
                        "📅 No classes added yet"

                    emptyText.textSize = 18f
                    emptyText.gravity = Gravity.CENTER
                    emptyText.setPadding(20, 30, 20, 30)

                    timetableContainer.addView(emptyText)

                    return@withContext
                }

                var currentDay = ""

                for (item in list) {

                    if (currentDay != item.day) {

                        currentDay = item.day

                        val dayTitle =
                            TextView(this@TimetableActivity)

                        dayTitle.text = currentDay
                        dayTitle.textSize = 20f
                        dayTitle.setTextColor(
                            resources.getColor(
                                android.R.color.black,
                                theme
                            )
                        )
                        dayTitle.setPadding(0, 20, 0, 8)

                        timetableContainer.addView(dayTitle)
                    }

                    val card =
                        CardView(this@TimetableActivity)

                    card.radius = 18f
                    card.cardElevation = 3f

                    val layout =
                        LinearLayout(this@TimetableActivity)

                    layout.orientation =
                        LinearLayout.VERTICAL

                    layout.setPadding(
                        20,
                        16,
                        20,
                        16
                    )

                    val info =
                        TextView(this@TimetableActivity)

                    info.text =
                        "⏰ ${item.time}\n" +
                                "📚 ${item.subject}\n" +
                                "🏫 ${item.room}"

                    info.textSize = 17f

                    val deleteButton =
                        Button(this@TimetableActivity)

                    deleteButton.text =
                        "Remove Class"

                    deleteButton.setOnClickListener {

                        CoroutineScope(Dispatchers.IO).launch {

                            database.timetableDao()
                                .deleteTimetable(item)

                            withContext(Dispatchers.Main) {
                                loadTimetable()
                            }
                        }
                    }

                    layout.addView(info)
                    layout.addView(deleteButton)

                    card.addView(layout)

                    val params =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                    params.setMargins(0, 0, 0, 12)

                    timetableContainer.addView(
                        card,
                        params
                    )
                }
            }
        }
    }
}