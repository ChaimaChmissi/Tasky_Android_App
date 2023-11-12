package com.example.employeesmanagementsys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.employeesmanagementsys.databinding.ActivityAddProjectBinding

class AddProjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var db: EmployeesDatabasesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = EmployeesDatabasesHelper(this)

        binding.saveButton.setOnClickListener {
            val projectName = binding.NameText.text.toString()
            val projectDate = binding.DateText.text.toString()

            if (isValidInput(projectName, projectDate)) {
                val project = Project(0, projectName, projectDate)
                db.insertProject(project)
                finish()
                Toast.makeText(this, "Project Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidInput(name: String, date: String): Boolean {
        if (name.isEmpty()) {
            showError(binding.NameText, "Name is required")
            return false
        } else {
            clearError(binding.NameText)
        }

        if (date.isEmpty()) {
            showError(binding.DateText, "Date is required")
            return false
        } else if (!isValidDate(date)) {
            showError(binding.DateText, "Invalid date format: dd/MM/yyyy")
            return false
        } else {
            clearError(binding.DateText)
        }

        return true
    }

    private fun showError(editText: EditText, message: String) {
        editText.error = message
    }

    private fun clearError(editText: EditText) {
        editText.error = null
    }

    private fun isValidDate(date: String): Boolean {
        val datePattern = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$"
        return date.matches(Regex(datePattern))
    }
}
