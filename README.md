# StudyDesk

StudyDesk is a simple Android app made for students to keep some basic academic activities in one place.

I made this project as part of my Mobile Application Development assignment. The main idea was to create a small application using basic Android concepts such as activities, layouts, intents, buttons, user input and data sharing.

## Features

### 1. Dashboard
The dashboard is the main screen of the app. It gives access to all the main features.

- Timetable
- Assignment Manager
- Attendance Calculator

### 2. Timetable
This section shows the weekly class timetable from Monday to Friday.

The timetable contains subjects such as:

- Python
- DBMS
- Operating System
- DAA
- Software Engineering
- Computer Networks

### 3. Assignment Manager
The Assignment Manager is used to add and view assignments.

The user can enter:

- Assignment title
- Subject
- Due date

After adding an assignment, it is displayed on the screen. Multiple assignments can be added.

There is also a **Share Assignments** option which uses Android's sharing feature to share the assignment details.

### 4. Attendance Calculator
This section calculates attendance percentage using:

**Attendance = (Attended Classes / Total Classes) × 100**

It also checks for invalid input such as:

- Empty fields
- Total classes equal to 0
- Attended classes greater than total classes
- Negative values

## Technologies Used

- Kotlin
- XML
- Android Studio
- Android SDK
- Git
- GitHub

## Android Concepts Used

While making this project, I used the following Android concepts:

- Activities
- Intents
- XML layouts
- LinearLayout
- ScrollView
- CardView
- TextView
- EditText
- Button
- Toast
- Click listeners
- Input validation
- Basic calculations
- Android sharing using `ACTION_SEND`

## 🔄 Application Flow

```text
                    StudyDesk
                       │
                       ▼
                  🏠 Dashboard
                 /      |       \
                /       |        \
               ▼        ▼         ▼
          📅 Timetable 📝 Assignments 📊 Attendance
                         │              │
                         │              ▼
                         │       Calculate Percentage
                         │
                         ▼
                    Add & View
                   Assignments
                         │
                         ▼
                    Share Data

## Developer
**Vidhi**
B.Tech Computer Engineering
Ganpat University
U.V. Patel College of Engineering
