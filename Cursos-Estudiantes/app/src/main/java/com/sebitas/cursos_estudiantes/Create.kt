package com.sebitas.cursos_estudiantes

import android.annotation.SuppressLint
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

class Create : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var dbCourseStudents = DBCourseStudents(this)



        val btnCreateCourse = findViewById<Button>(R.id.btnCreateCourse)

        btnCreateCourse.setOnClickListener{

            val etNameCourse = findViewById<EditText>(R.id.etNameCourse).text.toString()
            val etCodeCourse = findViewById<EditText>(R.id.etCodeCourse).text.toString()
            val etCostCourse = findViewById<EditText>(R.id.etCostCourse).text.toString().toDoubleOrNull()

            if(etNameCourse.isNotBlank() && etCodeCourse.isNotBlank() && etCostCourse != null){
                val course = Course(0,etNameCourse, etCodeCourse, LocalDate.now(), 0, etCostCourse, mutableListOf())
                dbCourseStudents.insertCourse(course)
                Toast.makeText(this, etNameCourse + " added to database of Courses", Toast.LENGTH_LONG).show()
                navigateToCreate()
            } else{
                Toast.makeText(this, "Please complete all fields correctly", Toast.LENGTH_SHORT).show()
            }




        }

    }

    private fun navigateToCreate() {
        val intent = Intent(this, Courses::class.java)
        startActivity(intent)
    }


}
