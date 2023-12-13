// ProjectsAdapter.kt
package com.example.employeesmanagementsys

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
// ProjectsAdapter.kt

import android.os.Build
import android.app.Notification
class ProjectsAdapter(
    private var projects: List<Project>,
    private val context: Context
) : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(project: Project)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NameTextView: TextView = itemView.findViewById(R.id.NameTextView)
        val DateTextView: TextView = itemView.findViewById(R.id.DateTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.project_item, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.NameTextView.text = project.project_name
        holder.DateTextView.text = project.project_date

        holder.itemView.setOnClickListener {
            listener?.onItemClick(project)
        }

        holder.updateButton.setOnClickListener {
            // Open UpdateProjectActivity with the project ID
            val intent = Intent(context, UpdateProjectActivity::class.java)
            intent.putExtra("project_id", project._id)
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            // Call the deleteEmployee method from EmployeeRepository
            val projectId = project._id
            val projectRepository = ProjectRepository()

            // Show a confirmation dialog before deleting
            AlertDialog.Builder(context)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete this project?")
                .setPositiveButton("Yes") { _, _ ->
                    projectRepository.deleteProject(projectId,
                        onSuccess = {
                            // Handle successful deletion
                            Toast.makeText(context, "Project deleted successfully", Toast.LENGTH_SHORT).show()
                            val updatedList = projects.toMutableList()
                            updatedList.removeAt(position)
                            refreshData(updatedList)
                        },
                        onError = { error ->
                            // Handle deletion error
                            Toast.makeText(context, "Error deleting employee: $error", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun refreshData(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged()
    }
}
