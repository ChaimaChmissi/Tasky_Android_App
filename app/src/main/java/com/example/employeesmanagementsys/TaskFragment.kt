// TaskFragment.kt
package com.example.employeesmanagementsys

import TasksAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.FragmentTaskBinding
// TaskFragment.kt
class TaskFragment : Fragment() {
    private lateinit var binding: FragmentTaskBinding
    private lateinit var tasksAdapter: TasksAdapter
    private val taskRepository = TaskRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksAdapter = TasksAdapter(emptyList(), requireContext())
        binding.taskRecyclerView.adapter = tasksAdapter

        val selectedProjectName = arguments?.getString("selected_project_name")
        if (!selectedProjectName.isNullOrEmpty()) {
            fetchTasksForProjectName(selectedProjectName)
        }
    }

    private fun fetchTasksForProjectName(projectName: String) {
        Log.d("TaskFragment", "Fetching tasks for project: $projectName")
        taskRepository.getTasksByProjectName(
            projectName,
            onSuccess = { tasks ->
                requireActivity().runOnUiThread {
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
                Log.d("TaskFragment", "Fetched tasks successfully: $tasks")
            },
            onError = { error ->
                Log.e("TaskFragment", "Error fetching tasks for project: $error")
                Toast.makeText(
                    requireContext(),
                    "Error fetching tasks for project: $error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            startActivity(intent)
        }

    }
}