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

class EditCourseActivity : AppCompatActivity() {

    private lateinit var dbCourseStudents: DBCourseStudents

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_course)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbCourseStudents = DBCourseStudents(this)
        val courseId = intent.getIntExtra("ID_COURSE", 0)
        Log.d("EditCourseActivity", "Received course ID: $courseId")

        val course = dbCourseStudents.getCourseById(courseId)
        if (course != null) {
            Log.d("EditCourseActivity", "Loaded course: $course")
            findViewById<EditText>(R.id.editNameCourse).setText(course.name)
            findViewById<EditText>(R.id.editCodeCourse).setText(course.code)
            findViewById<EditText>(R.id.editCostCourse).setText(course.cost.toString())
        } else {
            Log.e("EditCourseActivity", "Course not found for ID: $courseId")
        }

        findViewById<Button>(R.id.btnEditCourse).setOnClickListener {
            // Actualiza el curso en la base de datos
            val updatedCourse = course?.copy(
                name = findViewById<EditText>(R.id.editNameCourse).text.toString(),
                cost = findViewById<EditText>(R.id.editCostCourse).text.toString().toDoubleOrNull() ?: course.cost
            )
            if (updatedCourse != null) {
                dbCourseStudents.updateCourse(updatedCourse, courseId)
                Log.d("EditCourseActivity", "Course updated: $updatedCourse")
            } else {
                Log.e("EditCourseActivity", "Failed to update course")
            }
            finish()
        }
    }
}
