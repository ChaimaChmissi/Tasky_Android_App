// UpdateProjectActivity.kt
package com.example.employeesmanagementsys

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.employeesmanagementsys.databinding.ActivityUpdateProjectBinding

class UpdateProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProjectBinding
    private lateinit var projectRepository: ProjectRepository
    private var projectId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize your ProjectRepository
        projectRepository = ProjectRepository()

        // Retrieve the project ID from the intent
        projectId = intent.getStringExtra("project_id") ?: ""

        // Set up a click listener for the update button
        binding.updateSaveButton.setOnClickListener {
            updateProject()
        }

        // Fetch project data using the project ID and populate the UI
        fetchProjectData()
    }

    private fun fetchProjectData() {
        // Implement the logic to fetch project data using the project ID
        projectRepository.getProjectById(
            projectId,
            onSuccess = { project ->
                // Check if the project is not null before populating the UI
                if (project != null) {
                    // Populate the UI with the fetched project data
                    binding.updateNameText.setText(project.project_name)
                    binding.updateDateText.setText(project.project_date)

                    // Add logic for other fields as needed
                } else {
                    // Handle the case where the project is null
                    Toast.makeText(
                        this,
                        "Error: Project not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onError = { errorMessage ->
                // Handle the error, show a toast, log, or perform other error handling
                Toast.makeText(
                    this,
                    "Error fetching project data: $errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun updateProject() {
        // Retrieve updated data from UI fields
        val updatedName = binding.updateNameText.text.toString()
        val updatedDate = binding.updateDateText.text.toString()

        // Validate the input
        if (isValidInput(updatedName, updatedDate)) {
            val updatedProject = Project(projectId, updatedName, updatedDate)

            // Call the updateProject method in the repository to update the project
            projectRepository.updateProject(
                projectId,
                updatedProject,
                onSuccess = {
                    // Project updated successfully
                    Toast.makeText(this, "Project updated successfully", Toast.LENGTH_SHORT).show()

                    // Trigger a refresh of the project list
                    projectRepository.getProjects(
                        onSuccess = { projects ->
                            // Update your UI or perform any other actions with the updated project list
                            Toast.makeText(this, "Project list refreshed", Toast.LENGTH_SHORT).show()
                        },
                        onError = { error ->
                            Toast.makeText(this, "Error fetching projects: $error", Toast.LENGTH_SHORT).show()
                        }
                    )

                    finish() // Finish the current activity after updating
                },
                onError = { errorMessage ->
                    // Handle the error, show a toast, log, or perform other error handling
                    Toast.makeText(this, "Error updating project: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun isValidInput(name: String, date: String): Boolean {
        // Implement your validation logic here
        // Return true if the input is valid, false otherwise
        return true
    }
}
