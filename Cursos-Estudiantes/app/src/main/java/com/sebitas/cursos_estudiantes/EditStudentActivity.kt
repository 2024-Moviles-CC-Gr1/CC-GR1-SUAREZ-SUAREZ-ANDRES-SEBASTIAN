package com.sebitas.cursos_estudiantes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditStudentActivity : AppCompatActivity() {

    private lateinit var dbCourseStudents: DBCourseStudents

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbCourseStudents = DBCourseStudents(this)
        val studentId = intent.getIntExtra("STUDENT_ID", 0)
        Log.d("EditStudentActivity", "Received student ID: $studentId")

        val student = dbCourseStudents.getStudentById(studentId)
        if (student != null) {
            Log.d("EditCourseActivity", "Loaded course: $student")
            findViewById<EditText>(R.id.editNameStudent).setText(student.name)
            findViewById<EditText>(R.id.editCodeStudent).setText(student.code)
            findViewById<EditText>(R.id.editAgeStudent).setText(student.age.toString())
        } else {
            Log.e("EditStudentActivity", "Student not found for ID: $studentId")
        }

        findViewById<Button>(R.id.btnEditStudent).setOnClickListener {
            // Actualiza el curso en la base de datos
            val updatedStudent = student?.copy(
                name = findViewById<EditText>(R.id.editNameStudent).text.toString(),
                age = findViewById<EditText>(R.id.editAgeStudent).text.toString().toIntOrNull() ?: student.age
            )
            if (updatedStudent != null) {
                dbCourseStudents.updateStudent(updatedStudent, studentId)
                Log.d("EditStudentActivity", "Student updated: $updatedStudent")
            } else {
                Log.e("EditStudentActivity", "Failed to update student")
            }
            finish()
        }
    }
}