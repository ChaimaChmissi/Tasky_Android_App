// TaskFragment.kt
package com.example.employeesmanagementsys


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.TaskEmployeeActivityBinding

// TaskFragment.kt
class TaskEmployActivity : AppCompatActivity() {
    private lateinit var binding: TaskEmployeeActivityBinding
    private lateinit var tasksAdapter: TasksEmployAdapter
    private lateinit var sessionManager: SessionManager
    private val taskRepository = TaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TaskEmployeeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = TasksEmployAdapter(emptyList(), this)
        binding.taskRecyclerView.adapter = tasksAdapter

        // Initialize session manager
        sessionManager = SessionManager(this)

        fetchTasksByAssignedUser()
    }

    private fun fetchTasksByAssignedUser() {
        val assignedTo = sessionManager.getUserName()
        Log.d("TaskEmployActivity", "Fetching tasks for user: $assignedTo")
        taskRepository.getTasksByAssignedUser(
            assignedTo,
            onSuccess = { tasks ->
                runOnUiThread {
                    if (tasks.isEmpty()) {
                        binding.textNoTasks.visibility = View.VISIBLE
                        binding.taskRecyclerView.visibility = View.GONE
                        binding.ListTas.visibility = View.GONE
                    } else {
                        binding.textNoTasks.visibility = View.GONE
                        binding.taskRecyclerView.visibility = View.VISIBLE
                        binding.ListTas.visibility = View.VISIBLE
                        tasksAdapter.refreshData(tasks)
                    }
                }
                Log.d("TaskEmployActivity", "Fetched tasks successfully: $tasks")
            },
            onError = { error ->
                Log.e("TaskEmployActivity", "Error fetching tasks: $error")
                Toast.makeText(
                    this,
                    "Error fetching tasks: $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}