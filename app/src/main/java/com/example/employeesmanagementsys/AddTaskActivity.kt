package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.employeesmanagementsys.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var taskRepository: TaskRepository
    private lateinit var employeeList: List<Employee>
    private lateinit var projectList: List<Project>
    private lateinit var employeeApi: EmployeeApi
    private lateinit var projectApi: ProjectApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = TaskRepository()
        employeeApi = EmployeeApi(this)
        projectApi = ProjectApi(this)

        // Fetch employees and projects
        fetchEmployees()
        fetchProjects()

        // Set up status spinner options
        val statusOptions = listOf("Complete", "Incomplete")
        setupSpinner(binding.taskStatusSpinner, statusOptions)

        // Observe changes in the task list
        taskRepository.taskListLiveData.observe(this, Observer { tasks ->
            // Update your UI or perform any other actions with the updated task list
            Toast.makeText(this, "Task list refreshed automatically", Toast.LENGTH_SHORT).show()
        })

        binding.saveButton.setOnClickListener {
            saveTask()
        }
    }

    private fun setupSpinner(spinner: Spinner, options: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun fetchEmployees() {
        employeeApi.getEmployees(
            successListener = { employees ->
                employeeList = employees
                val employeeNames = employeeList.map { it.employee_name }
                setupSpinner(binding.assignedToSpinner, employeeNames)
            },
            errorListener = { error ->
                Toast.makeText(this, "Error fetching employees: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun fetchProjects() {
        projectApi.getProjects(
            successListener = { projects ->
                projectList = projects
                val projectNames = projectList.map { it.project_name }
                setupSpinner(binding.projectNameSpinner, projectNames)
            },
            errorListener = { error ->
                Toast.makeText(this, "Error fetching projects: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun getSelectedSpinnerValue(spinner: Spinner): String {
        return spinner.selectedItem.toString()
    }

    private fun getSelectedEmployee(spinner: Spinner): Employee {
        val selectedEmployeeName = getSelectedSpinnerValue(spinner)
        return employeeList.first { it.employee_name == selectedEmployeeName }
    }

    private fun getSelectedProject(spinner: Spinner): Project {
        val selectedProjectName = getSelectedSpinnerValue(spinner)
        return projectList.first { it.project_name == selectedProjectName }
    }

    private fun isValidInput(name: String, description: String): Boolean {
        if (name.isEmpty()) {
            showError(binding.TitleText, "Title is required")
            return false
        } else {
            clearError(binding.TitleText)
        }

        if (description.isEmpty()) {
            showError(binding.DescriptionText, "Description is required")
            return false
        } else {
            clearError(binding.DescriptionText)
        }

        return true
    }

    private fun showError(editText: EditText, message: String) {
        editText.error = message
    }

    private fun clearError(editText: EditText) {
        editText.error = null
    }
    private fun saveTask() {
        val taskTitle = binding.TitleText.text.toString()
        val taskDescription = binding.DescriptionText.text.toString()

        if (isValidInput(taskTitle, taskDescription)) {
            val assignedTo = getSelectedEmployee(binding.assignedToSpinner)
            val projectName = getSelectedProject(binding.projectNameSpinner)

            val task = Task(
                _id = "",
                task_title = taskTitle,
                task_description = taskDescription,
                task_status = getSelectedSpinnerValue(binding.taskStatusSpinner),
                assignedTo = assignedTo.employee_name,
                projectName = projectName.project_name
            )

            taskRepository.addTask(
                task,
                onSuccess = {
                    Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()

                    // Navigate back to the previous screen (fragment)
                    onBackPressed()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, "Error adding task: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

}
