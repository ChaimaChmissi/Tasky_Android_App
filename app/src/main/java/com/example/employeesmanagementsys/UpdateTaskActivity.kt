package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.employeesmanagementsys.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var taskRepository: TaskRepository
    private lateinit var employeeList: List<Employee>
    private lateinit var projectList: List<Project>
    private lateinit var employeeApi: EmployeeApi
    private lateinit var projectApi: ProjectApi

    private var selectedProject: String? = null
    private val statusOptions = listOf("Complete", "Incomplete")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskRepository = TaskRepository()
        employeeApi = EmployeeApi(this)
        projectApi = ProjectApi(this)

        // Fetch employees and projects sequentially
        fetchEmployees {
            fetchProjects {
                // Continue with other setup
                setupSpinner(binding.taskStatusSpinner, statusOptions)

                // Observe changes in the task list
                taskRepository.taskListLiveData.observe(this, Observer { tasks ->
                    // Update your UI or perform any other actions with the updated task list
                    Toast.makeText(this, "Task list refreshed automatically", Toast.LENGTH_SHORT).show()
                })

                // Fetch task details using the task ID
                val taskId = intent.getStringExtra("task_id") ?: ""
                fetchTaskDetails(taskId)
            }
        }

        binding.updateButton.setOnClickListener {
            updateTask()
        }
    }

    private fun setupSpinner(spinner: Spinner, options: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun fetchEmployees(onComplete: () -> Unit) {
        employeeApi.getEmployees(
            successListener = { employees ->
                employeeList = employees
                val employeeNames = employeeList.map { it.employee_name }
                setupSpinner(binding.assignedToSpinner, employeeNames)
                onComplete.invoke()
            },
            errorListener = { error ->
                Toast.makeText(this, "Error fetching employees: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun fetchProjects(onComplete: () -> Unit) {
        projectApi.getProjects(
            successListener = { projects ->
                projectList = projects
                val projectNames = projectList.map { it.project_name }
                setupSpinner(binding.projectNameSpinner, projectNames)
                onComplete.invoke()
            },
            errorListener = { error ->
                Toast.makeText(this, "Error fetching projects: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun fetchTaskDetails(taskId: String) {
        taskRepository.getTaskById(
            taskId,
            onSuccess = { task ->
                // Set initial values for UI elements based on the fetched task details
                binding.TitleText.setText(task.task_title)
                binding.DescriptionText.setText(task.task_description)

                // Set the selected project for further reference
                selectedProject = task.projectName

                // Set initial values for spinners based on the fetched task details
                val assignedToIndex = employeeList.indexOfFirst { it.employee_name == task.assignedTo }

                binding.assignedToSpinner.setSelection(if (assignedToIndex >= 0) assignedToIndex else 0)

                val projectIndex = projectList.indexOfFirst { it.project_name == task.projectName }
                binding.projectNameSpinner.setSelection(if (projectIndex >= 0) projectIndex else 0)

                val statusIndex = statusOptions.indexOf(task.task_status)
                binding.taskStatusSpinner.setSelection(if (statusIndex >= 0) statusIndex else 0)
            },
            onError = { error ->
                Toast.makeText(this, "Error fetching task details: $error", Toast.LENGTH_SHORT).show()
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

    private fun updateTask() {
        val taskTitle = binding.TitleText.text.toString()
        val taskDescription = binding.DescriptionText.text.toString()

        if (isValidInput(taskTitle, taskDescription)) {
            val assignedTo = getSelectedEmployee(binding.assignedToSpinner)
            val projectName = getSelectedProject(binding.projectNameSpinner)

            val updatedTask = Task(
                _id = intent.getStringExtra("task_id") ?: "",
                task_title = taskTitle,
                task_description = taskDescription,
                task_status = getSelectedSpinnerValue(binding.taskStatusSpinner),
                assignedTo = assignedTo.employee_name,
                projectName = projectName.project_name
            )

            taskRepository.updateTask(
                taskId = updatedTask._id,
                task = updatedTask,
                onSuccess = {
                    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
                    finish()  // Close the activity after updating the task
                },
                onError = { errorMessage ->
                    Toast.makeText(this, "Error updating task: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
