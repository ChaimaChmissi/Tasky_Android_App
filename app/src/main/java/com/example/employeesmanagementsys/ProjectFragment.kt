package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.FragmentProjectBinding
// ProjectsFragment.kt

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.os.Build
import android.content.Context

class ProjectFragment : Fragment(), ProjectsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentProjectBinding
    private lateinit var projectsAdapter: ProjectsAdapter
    private val projectRepository = ProjectRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Refresh your data here
            fetchProjects()
        }

        binding.projectRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        projectsAdapter = ProjectsAdapter(emptyList(), requireContext())
        binding.projectRecyclerView.adapter = projectsAdapter

        projectsAdapter.setOnItemClickListener(this)

        // Fetch projects from the API using Retrofit
        fetchProjects()

        // Replace this with the logic to add a new project
        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddProjectActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onItemClick(project: Project) {
        // This method will be called when a project is clicked
        Log.d("ProjectsFragment", "Clicked on project: ${project.project_name}")

        // Navigate to TaskFragment and pass the selected project name
        val taskFragment = TaskFragment()
        val bundle = Bundle()
        bundle.putString("selected_project_name", project.project_name.trim())
        taskFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, taskFragment)
            .addToBackStack(null)
            .commit()
    }


    private fun fetchProjects() {
        projectRepository.getProjects(
            onSuccess = { projects ->
                // Update UI or perform any other actions with the updated project list
                projectsAdapter.refreshData(projects)
                binding.swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
            },
            onError = { error ->
                Toast.makeText(requireContext(), "Error fetching projects: $error", Toast.LENGTH_SHORT).show()
                binding.swipeRefreshLayout.isRefreshing = false // Stop the refreshing animation
            }
        )
    }
}
