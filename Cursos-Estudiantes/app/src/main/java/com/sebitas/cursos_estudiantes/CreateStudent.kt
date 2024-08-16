package com.sebitas.cursos_estudiantes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate

class CreateStudent : AppCompatActivity() {

    private lateinit var dbCourseStudents: DBCourseStudents

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbCourseStudents = DBCourseStudents(this)



        val btnCreateStudent = findViewById<Button>(R.id.btnCreateStudent)

        btnCreateStudent.setOnClickListener{

            val etNameStudent = findViewById<EditText>(R.id.etNameStuent).text.toString()
            val etCodeStudent = findViewById<EditText>(R.id.etCodeStudent).text.toString()
            val etAgeStudent = findViewById<EditText>(R.id.etEageStudent).text.toString().toIntOrNull()

            if(etNameStudent.isNotBlank() && etCodeStudent.isNotBlank() && etAgeStudent != null){
                val student = Student(0,etNameStudent, etAgeStudent, etCodeStudent, LocalDate.now(), true)
                dbCourseStudents.insertStudent(student)
                Toast.makeText(this, etNameStudent + " added to database of Students", Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(this, "Please complete all fields correctly", Toast.LENGTH_SHORT).show()
            }

            navigateToCreate()
        }

    }
    private fun navigateToCreate() {
        val intent = Intent(this, Courses::class.java)
        startActivity(intent)
    }
}