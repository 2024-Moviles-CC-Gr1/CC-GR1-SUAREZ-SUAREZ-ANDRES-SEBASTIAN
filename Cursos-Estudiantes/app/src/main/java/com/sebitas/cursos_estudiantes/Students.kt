package com.sebitas.cursos_estudiantes

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Students : AppCompatActivity() {

    private lateinit var studentsContainer: LinearLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnOpenMaps = findViewById<Button>(R.id.btnOpenMaps)
        btnOpenMaps.setOnClickListener {
            // Uri para abrir una localización específica (latitud y longitud)
            val gmmIntentUri = Uri.parse("geo:0,0?q=-0.180653, -78.467834(Quito)")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        }

        val btnCreateStudent = findViewById<Button>(R.id.btnCreateStudent)
        btnCreateStudent.setOnClickListener {
            navigateToCreateStudent()
        }

        studentsContainer = findViewById(R.id.studentsContainer)

        val dbCourseStudents = DBCourseStudents(this)
        val students = dbCourseStudents.getAllStudents()
        addStudentButtons(students)


    }
    private fun navigateToCreateStudent() {
        val intent = Intent(this, CreateStudent::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addStudentButtons(students: List<Student>) {
        for (student in students) {
            val button = Button(this).apply {
                text = student.name
                setOnClickListener {
                    Toast.makeText(this@Students, "Selected Student: ${student.name}", Toast.LENGTH_SHORT).show()
                    // Aquí puedes agregar cualquier acción adicional que desees realizar al hacer clic en el botón
                    openGoogleMaps(-2.196160, -79.886207)
                }
                setOnLongClickListener {
                    showAdditionalOptions(student)
                    true // Indica que se ha consumido el evento de presionar largo
                }
            }
            studentsContainer.addView(button)
        }
    }

    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAdditionalOptions(student: Student) {

        val option1 = Button(this).apply {
            text = "Editar"
            setOnClickListener {
                Toast.makeText(this@Students, "Opción Editar para ${student.name} seleccionada", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@Students, EditStudentActivity::class.java)
                intent.putExtra("STUDENT_ID", student.id)
                startActivity(intent)
            }
        }
        val dbCourseStudents = DBCourseStudents(this)

        val option2 = Button(this).apply {
            text = "Eliminar"
            setOnClickListener {
                dbCourseStudents.deleteStudent(student.code)
                Toast.makeText(this@Students, "Estudiante ${student.name} eliminado", Toast.LENGTH_SHORT).show()
                refreshStudents()
            }
        }


        // Limpia el contenedor antes de agregar las nuevas opciones
        studentsContainer.removeAllViews()

        // Agrega los botones de opciones adicionales
        studentsContainer.addView(option1)
        studentsContainer.addView(option2)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun refreshStudents() {
        // Vuelve a cargar los cursos de la base de datos y actualiza la vista
        val dbCourseStudents = DBCourseStudents(this)
        val students = dbCourseStudents.getAllStudents()
        studentsContainer.removeAllViews()
        addStudentButtons(students)
    }
}