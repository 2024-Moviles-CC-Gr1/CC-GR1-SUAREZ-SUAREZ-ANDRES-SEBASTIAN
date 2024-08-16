package com.sebitas.cursos_estudiantes

// models.kt
import java.time.LocalDate

data class Student(
    val id: Int = 0,
    val name: String,
    val age: Int,
    val code: String,
    val enrollmentDate: LocalDate,
    val isScholarshipHolder: Boolean
)

data class Course(
    val id: Int = 0,
    val name: String,
    val code: String,
    val startDate: LocalDate,
    var studentCount: Int,
    val cost: Double?,
    val students: MutableList<Student> = mutableListOf()
)

