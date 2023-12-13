// TasksAdapter.kt
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
import com.example.employeesmanagementsys.R
import com.example.employeesmanagementsys.Task
import com.example.employeesmanagementsys.TaskRepository
import com.example.employeesmanagementsys.UpdateTaskActivity

class TasksAdapter(
    private var tasks: List<Task>,
    private val context: Context
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TitleTextView: TextView = itemView.findViewById(R.id.TitleTextView)
        val DescriptionTextView: TextView = itemView.findViewById(R.id.DescriptionTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.TitleTextView.text = task.task_title
        holder.DescriptionTextView.text = task.task_description

        holder.updateButton.setOnClickListener {
            // Open UpdateTaskActivity with the task ID
            val intent = Intent(context, UpdateTaskActivity::class.java)
            intent.putExtra("task_id", task._id)
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            // Call the deleteTask method from TaskRepository
            val taskId = task._id
            val taskRepository = TaskRepository()

            // Show a confirmation dialog before deleting
            AlertDialog.Builder(context)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Yes") { _, _ ->
                    taskRepository.deleteTask(taskId,
                        onSuccess = {
                            // Handle successful deletion
                            Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                            val updatedList = tasks.toMutableList()
                            updatedList.removeAt(position)
                            refreshData(updatedList)
                        },
                        onError = { error ->
                            // Handle deletion error
                            Toast.makeText(context, "Error deleting task: $error", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                .setNegativeButton("No", null)
                .show()
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
