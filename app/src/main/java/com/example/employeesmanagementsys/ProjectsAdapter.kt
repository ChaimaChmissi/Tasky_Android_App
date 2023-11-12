package com.example.employeesmanagementsys


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ProjectsAdapter(
    private var projects: List<Project>,
    private val context: Context

) : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    private val db: EmployeesDatabasesHelper = EmployeesDatabasesHelper(context)

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

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateProjectActivity::class.java).apply {
                putExtra("project_id", project.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(project)
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    fun refreshData(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged()
    }

    private fun showDeleteConfirmationDialog(project: Project) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("Delete project")
        dialogBuilder.setMessage("Are you sure you want to delete this project?")
        dialogBuilder.setPositiveButton("Delete") { _, _ ->
            db.deleteProject(project.id)
            refreshData(db.getAllProjects())
            Toast.makeText(context, "project Deleted", Toast.LENGTH_SHORT).show()
        }
        dialogBuilder.setNegativeButton("Cancel") { _, _ -> }
        dialogBuilder.create().show()
    }
}
