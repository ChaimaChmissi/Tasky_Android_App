package com.example.employeesmanagementsys

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class EmployeesDatabasesHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "employeeapp.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_NAME = "employee_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "employee_name"
        private const val COLUMN_EMAIL = "employee_email"
        private const val COLUMN_PASSWORD = "employee_password"

        // Nouvelle table pour les projets
        private const val PROJECT_TABLE_NAME = "project_table"
        private const val PROJECT_COLUMN_ID = "id"
        private const val PROJECT_COLUMN_NAME = "project_name"
        private const val PROJECT_COLUMN_DATE = "project_date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createEmployeeTableQuery = "CREATE TABLE $TABLE_NAME " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EMAIL TEXT, " +
                "$COLUMN_PASSWORD TEXT);"
        db?.execSQL(createEmployeeTableQuery)

        // Créer la nouvelle table pour les projets
        val createProjectTableQuery = "CREATE TABLE $PROJECT_TABLE_NAME " +
                "($PROJECT_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PROJECT_COLUMN_NAME TEXT, " +
                "$PROJECT_COLUMN_DATE TEXT);"
        db?.execSQL(createProjectTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropEmployeeTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropEmployeeTableQuery)

        // Supprimer la table des projets lors de la mise à jour
        val dropProjectTableQuery = "DROP TABLE IF EXISTS $PROJECT_TABLE_NAME"
        db?.execSQL(dropProjectTableQuery)

        onCreate(db)
    }

    fun insertEmployee(employee: Employee) {
        val db = writableDatabase
        val values = contentValuesOf().apply {
            put(COLUMN_NAME, employee.employee_name)
            put(COLUMN_EMAIL, employee.employee_mail)
            put(COLUMN_PASSWORD, employee.employee_password)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllEmployees(): List<Employee> {
        val employeesList = mutableListOf<Employee>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val employee_name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val employee_mail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val employee_password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))

            val employee = Employee(id, employee_name, employee_mail, employee_password)
            employeesList.add(employee)
        }
        cursor.close()
        db.close()
        return employeesList
    }

    fun updateEmployee(employee: Employee) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.employee_name)
            put(COLUMN_EMAIL, employee.employee_mail)
            put(COLUMN_PASSWORD, employee.employee_password)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(employee.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getEmployeeByID(employeeId: Int): Employee {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(employeeId.toString()))
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val employee_name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val employee_mail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val employee_password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))

        cursor.close()
        db.close()
        return Employee(id, employee_name, employee_mail, employee_password)
    }

    fun deleteEmployee(employeeId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(employeeId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    // Méthodes pour la gestion des projets

    fun insertProject(project: Project) {
        val db = writableDatabase
        val values = contentValuesOf().apply {
            put(PROJECT_COLUMN_NAME, project.project_name)
            put(PROJECT_COLUMN_DATE, project.project_date)
        }
        db.insert(PROJECT_TABLE_NAME, null, values)
        db.close()
    }

    fun getAllProjects(): List<Project> {
        val projectsList = mutableListOf<Project>()
        val db = readableDatabase
        val query = "SELECT * FROM $PROJECT_TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECT_COLUMN_ID))
            val project_name = cursor.getString(cursor.getColumnIndexOrThrow(PROJECT_COLUMN_NAME))
            val project_date = cursor.getString(cursor.getColumnIndexOrThrow(PROJECT_COLUMN_DATE))

            val project = Project(id, project_name, project_date)
            projectsList.add(project)
        }
        cursor.close()
        db.close()
        return projectsList
    }

    fun deleteProject(projectId: Int) {
        val db = writableDatabase
        val whereClause = "$PROJECT_COLUMN_ID = ?"
        val whereArgs = arrayOf(projectId.toString())
        db.delete(PROJECT_TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getProjectByID(projectId: Int): Project {
        val db = readableDatabase
        val query = "SELECT * FROM $PROJECT_TABLE_NAME WHERE $PROJECT_COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(projectId.toString()))
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(PROJECT_COLUMN_ID))
        val project_name = cursor.getString(cursor.getColumnIndexOrThrow(PROJECT_COLUMN_NAME))
        val project_date = cursor.getString(cursor.getColumnIndexOrThrow(PROJECT_COLUMN_DATE))

        cursor.close()
        db.close()
        return Project(id, project_name, project_date)
    }

    fun updateProject(project: Project) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(PROJECT_COLUMN_NAME, project.project_name)
            put(PROJECT_COLUMN_DATE, project.project_date)
        }
        val whereClause = "$PROJECT_COLUMN_ID = ?"
        val whereArgs = arrayOf(project.id.toString())
        db.update(PROJECT_TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }
}
