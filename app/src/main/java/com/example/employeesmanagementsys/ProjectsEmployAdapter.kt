// ProjectsAdapter.kt
package com.example.employeesmanagementsys

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
class ProjectsEmployAdapter(
    private var projects: List<Project>,
    private val context: Context
) : RecyclerView.Adapter<ProjectsEmployAdapter.ProjectViewHolder>() {

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

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.project_employee_item, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.NameTextView.text = project.project_name
        holder.DateTextView.text = project.project_date

        holder.itemView.setOnClickListener {
            listener?.onItemClick(project)
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
