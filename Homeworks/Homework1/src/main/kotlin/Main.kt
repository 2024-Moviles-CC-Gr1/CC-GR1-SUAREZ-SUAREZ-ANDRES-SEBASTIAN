import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Scanner
import java.io.File

data class Student(
    val name: String,
    val age: Int,
    val enrollment: String,
    val enrollmentDate: LocalDate,
    val isScholarshipHolder: Boolean
)

data class Course(
    val name: String,
    val code: String,
    val startDate: LocalDate,
    var studentCount: Int,
    val cost: Double,
    val students: MutableList<Student>
)

val courses = mutableListOf<Course>()

val gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
    .create()

val scanner = Scanner(System.`in`)

class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(src: LocalDate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LocalDate {
        return LocalDate.parse(json.asString, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

fun createCourse(course: Course) {
    courses.add(course)
    saveData()
}

fun readCourses(): List<Course> {
    return courses
}

fun updateCourse(index: Int, course: Course) {
    if (index in courses.indices) {
        courses[index] = course
        saveData()
    } else {
        println("Invalid index.")
    }
}

fun deleteCourse(index: Int) {
    if (index in courses.indices) {
        courses.removeAt(index)
        saveData()
    } else {
        println("Invalid index.")
    }
}

fun saveData() {
    val jsonString = gson.toJson(courses)
    File("courses.json").writeText(jsonString)
}

fun loadData() {
    val file = File("courses.json")
    if (file.exists()) {
        val jsonString = file.readText()
        val listType = object : TypeToken<MutableList<Course>>() {}.type
        val savedCourses: MutableList<Course> = gson.fromJson(jsonString, listType)
        courses.clear()
        courses.addAll(savedCourses)
    }
}

fun createCourseFromConsole() {
    println("Enter the course name:")
    scanner.nextLine() // Consume the new line
    val name = scanner.nextLine()
    println("Enter the course code:")
    val code = scanner.nextLine()
    println("Enter the course start date (yyyy-MM-dd):")
    val startDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE)
    println("Enter the number of students:")
    val studentCount = scanner.nextInt()
    println("Enter the course cost:")
    val cost = scanner.nextDouble()
    scanner.nextLine() // Consume the new line

    val students = mutableListOf<Student>()
    for (i in 1..studentCount) {
        println("Enter the name of student $i:")
        val studentName = scanner.nextLine()
        println("Enter the age of student $i:")
        val age = scanner.nextInt()
        scanner.nextLine() // Consume the new line
        println("Enter the enrollment of student $i:")
        val enrollment = scanner.nextLine()
        println("Enter the enrollment date of student $i (yyyy-MM-dd):")
        val enrollmentDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE)
        println("Is student $i a scholarship holder? (true/false):")
        val isScholarshipHolder = scanner.nextBoolean()
        scanner.nextLine() // Consume the new line

        val student = Student(studentName, age, enrollment, enrollmentDate, isScholarshipHolder)
        students.add(student)
    }

    val course = Course(name, code, startDate, studentCount, cost, students)
    createCourse(course)
    println("Course created successfully.")
}

fun readCoursesFromConsole() {
    val allCourses = readCourses()
    allCourses.forEach { println(it) }
}

fun updateCourseFromConsole() {
    println("Enter the index of the course to update:")
    val index = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (index !in courses.indices) {
        println("Invalid index.")
        return
    }

    val currentCourse = courses[index]

    println("Enter the new course name (current: ${currentCourse.name}):")
    val name = scanner.nextLine()
    println("Enter the new course code (current: ${currentCourse.code}):")
    val code = scanner.nextLine()
    println("Enter the new course start date (current: ${currentCourse.startDate}, yyyy-MM-dd):")
    val startDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE)
    println("Enter the new number of students (current: ${currentCourse.studentCount}):")
    val studentCount = scanner.nextInt()
    println("Enter the new course cost (current: ${currentCourse.cost}):")
    val cost = scanner.nextDouble()
    scanner.nextLine() // Consume the new line

    val students = mutableListOf<Student>()
    for (i in 1..studentCount) {
        println("Enter the name of student $i:")
        val studentName = scanner.nextLine()
        println("Enter the age of student $i:")
        val age = scanner.nextInt()
        scanner.nextLine() // Consume the new line
        println("Enter the enrollment of student $i:")
        val enrollment = scanner.nextLine()
        println("Enter the enrollment date of student $i (yyyy-MM-dd):")
        val enrollmentDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE)
        println("Is student $i a scholarship holder? (true/false):")
        val isScholarshipHolder = scanner.nextBoolean()
        scanner.nextLine() // Consume the new line

        val student = Student(studentName, age, enrollment, enrollmentDate, isScholarshipHolder)
        students.add(student)
    }

    val newCourse = Course(name, code, startDate, studentCount, cost, students)
    updateCourse(index, newCourse)
    println("Course updated successfully.")
}

fun deleteCourseFromConsole() {
    println("Enter the index of the course to delete:")
    val index = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (index !in courses.indices) {
        println("Invalid index.")
        return
    }

    deleteCourse(index)
    println("Course deleted successfully.")
}

fun createStudentInCourseFromConsole() {
    println("Enter the index of the course where you want to add a student:")
    val indexCourse = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (indexCourse !in courses.indices) {
        println("Invalid course index.")
        return
    }

    println("Enter the student's name:")
    val name = scanner.nextLine()
    println("Enter the student's age:")
    val age = scanner.nextInt()
    scanner.nextLine() // Consume the new line
    println("Enter the student's enrollment:")
    val enrollment = scanner.nextLine()
    println("Enter the student's enrollment date (yyyy-MM-dd):")
    val enrollmentDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE)
    println("Is the student a scholarship holder? (true/false):")
    val isScholarshipHolder = scanner.nextBoolean()
    scanner.nextLine() // Consume the new line

    val student = Student(name, age, enrollment, enrollmentDate, isScholarshipHolder)
    courses[indexCourse].students.add(student)
    courses[indexCourse] = courses[indexCourse].copy(studentCount = courses[indexCourse].students.size)
    saveData()
    println("Student added successfully.")
}

fun readStudentsFromCourseFromConsole() {
    println("Enter the index of the course from which you want to read the students:")
    val indexCourse = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (indexCourse !in courses.indices) {
        println("Invalid course index.")
        return
    }

    val students = courses[indexCourse].students
    students.forEach { println(it) }
}

fun updateStudentInCourseFromConsole() {
    println("Enter the index of the course from which you want to update a student:")
    val indexCourse = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (indexCourse !in courses.indices) {
        println("Invalid course index.")
        return
    }

    println("Enter the index of the student to update:")
    val indexStudent = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (indexStudent !in courses[indexCourse].students.indices) {
        println("Invalid student index.")
        return
    }

    val currentStudent = courses[indexCourse].students[indexStudent]

    println("Enter the new student name (current: ${currentStudent.name}):")
    val name = scanner.nextLine()
    println("Enter the new student age (current: ${currentStudent.age}):")
    val age = scanner.nextInt()
    scanner.nextLine() // Consume the new line
    println("Enter the new student enrollment (current: ${currentStudent.enrollment}):")
    val enrollment = scanner.nextLine()
    println("Enter the new student enrollment date (current: ${currentStudent.enrollmentDate}, yyyy-MM-dd):")
    val enrollmentDate = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE)
    println("Is the student a scholarship holder? (current: ${currentStudent.isScholarshipHolder}, true/false):")
    val isScholarshipHolder = scanner.nextBoolean()
    scanner.nextLine() // Consume the new line

    val newStudent = Student(name, age, enrollment, enrollmentDate, isScholarshipHolder)
    courses[indexCourse].students[indexStudent] = newStudent
    saveData()
    println("Student updated successfully.")
}

fun deleteStudentInCourseFromConsole() {
    println("Enter the index of the course from which you want to delete a student:")
    val indexCourse = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (indexCourse !in courses.indices) {
        println("Invalid course index.")
        return
    }

    println("Enter the index of the student to delete:")
    val indexStudent = scanner.nextInt()
    scanner.nextLine() // Consume the new line

    if (indexStudent !in courses[indexCourse].students.indices) {
        println("Invalid student index.")
        return
    }

    courses[indexCourse].students.removeAt(indexStudent)
    courses[indexCourse] = courses[indexCourse].copy(studentCount = courses[indexCourse].students.size)
    saveData()
    println("Student deleted successfully.")
}

fun main() {
    loadData()

    var option: Int
    do {
        println("\n--------------Course-Student Application Menu------------\n")
        println("1. Create Course")
        println("2. Read Courses")
        println("3. Update Course")
        println("4. Delete Course")
        println("5. Create Student in Course")
        println("6. Read Students from Course")
        println("7. Update Student in Course")
        println("8. Delete Student in Course")
        println("9. Exit")
        println("Choose an option:")
        option = scanner.nextInt()

        when (option) {
            1 -> createCourseFromConsole()
            2 -> readCoursesFromConsole()
            3 -> updateCourseFromConsole()
            4 -> deleteCourseFromConsole()
            5 -> createStudentInCourseFromConsole()
            6 -> readStudentsFromCourseFromConsole()
            7 -> updateStudentInCourseFromConsole()
            8 -> deleteStudentInCourseFromConsole()
            9 -> println("Exiting...")
            else -> println("Invalid option. Please try again.")
        }
    } while (option != 9)
}
