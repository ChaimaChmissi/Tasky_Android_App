package com.example.employeesmanagementsys


// TasksAdapter.kt
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TasksEmployAdapter(
    private var tasks: List<Task>,
    private val context: Context
) : RecyclerView.Adapter<TasksEmployAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TitleTextView: TextView = itemView.findViewById(R.id.TitleTextView)
        val DescriptionTextView: TextView = itemView.findViewById(R.id.DescriptionTextView)
        val cbHeart: CheckBox = itemView.findViewById(R.id.cbHeart)

        fun bind(task: Task) {
            TitleTextView.text = task.task_title
            DescriptionTextView.text = task.task_description
            cbHeart.isChecked = task.task_status == "complete"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_employee_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)

        // Remove the listener to avoid recursion
        holder.cbHeart.setOnCheckedChangeListener(null)

        holder.cbHeart.isChecked = task.task_status == "complete"

        // Re-add the listener after updating the state
        holder.cbHeart.setOnCheckedChangeListener { _, isChecked ->
            val updatedTask = task.copy(task_status = if (isChecked) "complete" else "incomplete")

            TaskRepository().updateTaskStatus(
                taskId = task._id,
                task = updatedTask,
                onSuccess = {
                    task.task_status = updatedTask.task_status
                    notifyItemChanged(position)
                },
                onError = { error ->
                    // Log the actual response from the server
                }

            )
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    fun refreshData(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

}