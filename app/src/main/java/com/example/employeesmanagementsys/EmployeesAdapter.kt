package com.example.employeesmanagementsys


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
class EmployeesAdapter(
    private var employees: List<Employee>,
    private val context: Context

) : RecyclerView.Adapter<EmployeesAdapter.EmployeeViewHolder>() {

    class EmployeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val NameTextView: TextView = itemView.findViewById(R.id.NameTextView)
        val MailTextView: TextView = itemView.findViewById(R.id.MailTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false)
        return EmployeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee = employees[position]
        holder.NameTextView.text = employee.employee_name
        holder.MailTextView.text = employee.employee_mail

        holder.updateButton.setOnClickListener {
            // Open UpdateEmployeeActivity with the employee ID
            val intent = Intent(context, UpdateEmployeeActivity::class.java)
            intent.putExtra("employee_id", employee._id)
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            // Call the deleteEmployee method from EmployeeRepository
            val employeeId = employee._id
            val employeeRepository = EmployeeRepository()

            // Show a confirmation dialog before deleting
            AlertDialog.Builder(context)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Yes") { _, _ ->
                    employeeRepository.deleteEmployee(employeeId,
                        onSuccess = {
                            // Handle successful deletion
                            Toast.makeText(context, "Employee deleted successfully", Toast.LENGTH_SHORT).show()
                            val updatedList = employees.toMutableList()
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
        return employees.size
    }

    fun refreshData(newEmployees: List<Employee>) {
        employees = newEmployees
        notifyDataSetChanged()
    }
}
