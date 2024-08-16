package com.sebitas.cursos_estudiantes
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DBCourseStudents(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "school.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_STUDENT = "Student"
        private const val TABLE_COURSE = "Course"
        private const val TABLE_COURSE_STUDENT = "CourseStudent"

        private const val CREATE_TABLE_STUDENT = """
            CREATE TABLE $TABLE_STUDENT (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                code TEXT NOT NULL,
                enrollment_date TEXT NOT NULL,
                is_scholarship_holder INTEGER NOT NULL
            )
        """

        private const val CREATE_TABLE_COURSE = """
            CREATE TABLE $TABLE_COURSE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                code TEXT NOT NULL,
                start_date TEXT NOT NULL,
                student_count INTEGER NOT NULL,
                cost REAL NOT NULL
            )
        """

        private const val CREATE_TABLE_COURSE_STUDENT = """
            CREATE TABLE $TABLE_COURSE_STUDENT (
                course_id INTEGER NOT NULL,
                student_id INTEGER NOT NULL,
                FOREIGN KEY (course_id) REFERENCES $TABLE_COURSE(id),
                FOREIGN KEY (student_id) REFERENCES $TABLE_STUDENT(id),
                PRIMARY KEY (course_id, student_id)
            )
        """

        @RequiresApi(Build.VERSION_CODES.O)
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_STUDENT)
        db?.execSQL(CREATE_TABLE_COURSE)
        db?.execSQL(CREATE_TABLE_COURSE_STUDENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COURSE_STUDENT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COURSE")
        onCreate(db)
    }

    // CRUD for Student
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertStudent(student: Student): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", student.name)
            put("age", student.age)
            put("code", student.code)
            put("enrollment_date", student.enrollmentDate.format(dateFormatter))
            put("is_scholarship_holder", if (student.isScholarshipHolder) 1 else 0)
        }
        return db.insert(TABLE_STUDENT, null, contentValues)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllStudents(): List<Student> {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_STUDENT, null, null, null, null, null, null)
        val students = mutableListOf<Student>()
        if (cursor.moveToFirst()) {
            do {
                students.add(
                    Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("age")),
                        cursor.getString(cursor.getColumnIndexOrThrow("code")),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("enrollment_date")), dateFormatter),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_scholarship_holder")) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return students
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStudent(student: Student, id: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", student.name)
            put("age", student.age)
            put("code", student.code)
            put("enrollment_date", student.enrollmentDate.format(dateFormatter))
            put("is_scholarship_holder", if (student.isScholarshipHolder) 1 else 0)
        }
        return db.update(TABLE_STUDENT, contentValues, "id=?", arrayOf(id.toString()))
    }

    fun deleteStudent(code: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_STUDENT, "code=?", arrayOf(code))
    }

    // CRUD for Course
    @RequiresApi(Build.VERSION_CODES.O)
    fun insertCourse(course: Course): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", course.name)
            put("code", course.code)
            put("start_date", course.startDate.format(dateFormatter))
            put("student_count", course.studentCount)
            put("cost", course.cost)
        }
        return db.insert(TABLE_COURSE, null, contentValues)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllCourses(): List<Course> {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_COURSE, null, null, null, null, null, null)
        val courses = mutableListOf<Course>()
        if (cursor.moveToFirst()) {
            do {
                courses.add(
                    Course(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("code")),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("start_date")), dateFormatter),
                        cursor.getInt(cursor.getColumnIndexOrThrow("student_count")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("cost")),
                        mutableListOf()
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courses
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateCourse(course: Course, id: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("name", course.name)
            put("code", course.code)
            put("start_date", course.startDate.format(dateFormatter))
            put("student_count", course.studentCount)
            put("cost", course.cost)
        }
        return db.update(TABLE_COURSE, contentValues, "id=?", arrayOf(id.toString()))
    }

    fun deleteCourse(code: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_COURSE, "code=?", arrayOf(code))
    }

    // CRUD for CourseStudent
    fun addStudentToCourse(courseId: Int, studentId: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("course_id", courseId)
            put("student_id", studentId)
        }
        return db.insert(TABLE_COURSE_STUDENT, null, contentValues)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStudentsForCourse(courseId: Int): List<Student> {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT s.* FROM $TABLE_STUDENT s INNER JOIN $TABLE_COURSE_STUDENT cs ON s.id = cs.student_id WHERE cs.course_id = ?",
            arrayOf(courseId.toString())
        )
        val students = mutableListOf<Student>()
        if (cursor.moveToFirst()) {
            do {
                students.add(
                    Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("age")),
                        cursor.getString(cursor.getColumnIndexOrThrow("code")),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("enrollment_date")), dateFormatter),
                        cursor.getInt(cursor.getColumnIndexOrThrow("is_scholarship_holder")) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return students
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCourseById(courseId: Int): Course? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_COURSE,
            null,
            "id=?",
            arrayOf(courseId.toString()),
            null,
            null,
            null
        )

        var course: Course? = null
        if (cursor.moveToFirst()) {
            course = Course(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("code")),
                LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("start_date")), dateFormatter),
                cursor.getInt(cursor.getColumnIndexOrThrow("student_count")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("cost")),
                mutableListOf()
            )
        }
        cursor.close()
        return course
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStudentById(studentId: Int): Student? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_STUDENT,
            null,
            "id=?",
            arrayOf(studentId.toString()),
            null,
            null,
            null
        )

        var student: Student? = null
        if (cursor.moveToFirst()) {
            student = Student(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getInt(cursor.getColumnIndexOrThrow("age")),
                cursor.getString(cursor.getColumnIndexOrThrow("code")),
                LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("enrollment_date")), dateFormatter),
                cursor.getInt(cursor.getColumnIndexOrThrow("is_scholarship_holder")) == 1
            )
        }
        cursor.close()
        return student
    }

    fun removeStudentFromCourse(courseId: Int, studentId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_COURSE_STUDENT, "course_id=? AND student_id=?", arrayOf(courseId.toString(), studentId.toString()))
    }
}
