package com.sebitas.cursos_estudiantes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Courses : AppCompatActivity() {

    private lateinit var courseContainer: LinearLayout

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_courses)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCreateCourse = findViewById<Button>(R.id.btnCreate)
        btnCreateCourse.setOnClickListener {
            navigateToCreate()
        }

        val dbCourseStudents = DBCourseStudents(this)
        courseContainer = findViewById(R.id.courseContainer)

        val courses = dbCourseStudents.getAllCourses()
        addCourseButtons(courses)
    }

    private fun navigateToCreate() {
        val intent = Intent(this, Create::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addCourseButtons(courses: List<Course>) {
        for (course in courses) {
            val button = Button(this).apply {
                text = course.name
                setOnClickListener {
                    Toast.makeText(this@Courses, "Selected course: ${course.name}", Toast.LENGTH_SHORT).show()
                    // Aquí puedes agregar cualquier acción adicional que desees realizar al hacer clic en el botón
                }
                setOnLongClickListener {
                    showAdditionalOptions(course)
                    true // Indica que se ha consumido el evento de presionar largo
                }
            }
            courseContainer.addView(button)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAdditionalOptions(course: Course) {
        // Implementa aquí la lógica para mostrar opciones adicionales al mantener presionado el curso
        // Por ejemplo, puedes crear y mostrar botones adicionales
        val option1 = Button(this).apply {
            text = "Editar"
            setOnClickListener {
                Toast.makeText(this@Courses, "Opción Editar para ${course.name} seleccionada", Toast.LENGTH_SHORT).show()
                Log.d("Courses", "Edit button clicked for course: ${course.name}, ID: ${course.id}")
                val intentEdit = Intent(this@Courses, EditCourseActivity::class.java)
                intentEdit.putExtra("ID_COURSE", course.id)  // Asegúrate de que 'course.id' es correcto
                startActivity(intentEdit)
            }
        }
        val dbCourseStudents = DBCourseStudents(this)

        val option2 = Button(this).apply {
            text = "Eliminar"
            setOnClickListener {
                dbCourseStudents.deleteCourse(course.code)
                Toast.makeText(this@Courses, "Curso ${course.name} eliminado", Toast.LENGTH_SHORT).show()
                refreshCourses()
            }
        }

        val option3 = Button(this).apply {
            text = "Ver Estudiantes"
            setOnClickListener {
                Toast.makeText(this@Courses, "Opción Ver Estudiantes para ${course.name} seleccionada", Toast.LENGTH_SHORT).show()
                navigateToSeeStudent()
            }
        }

        // Limpia el contenedor antes de agregar las nuevas opciones
        courseContainer.removeAllViews()

        // Agrega los botones de opciones adicionales
        courseContainer.addView(option1)
        courseContainer.addView(option2)
        courseContainer.addView(option3)
    }

    private fun navigateToSeeStudent() {
        val intent = Intent(this, Students::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshCourses() {
        // Vuelve a cargar los cursos de la base de datos y actualiza la vista
        val dbCourseStudents = DBCourseStudents(this)
        val courses = dbCourseStudents.getAllCourses()
        courseContainer.removeAllViews()
        addCourseButtons(courses)
    }
}
