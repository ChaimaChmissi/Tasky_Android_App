package com.example.employeesmanagementsys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.employeesmanagementsys.databinding.ActivityAddEmployeeBinding

class AddEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEmployeeBinding
    private lateinit var db: EmployeesDatabasesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = EmployeesDatabasesHelper(this)

        binding.saveButton.setOnClickListener {
            val employeeName = binding.NameText.text.toString()
            val employeeEmail = binding.MailText.text.toString()
            val employeePassword = binding.PasswText.text.toString()

            if (isValidInput(employeeName, employeeEmail, employeePassword)) {
                val employee = Employee(0, employeeName, employeeEmail, employeePassword)
                db.insertEmployee(employee)
                finish()
                Toast.makeText(this, "Employee Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            showError(binding.NameText, "Name is required")
            return false
        } else {
            clearError(binding.NameText)
        }

        if (email.isEmpty()) {
            showError(binding.MailText, "Email is required")
            return false
        } else if (!isValidEmail(email)) {
            showError(binding.MailText, "Invalid email address")
            return false
        } else {
            clearError(binding.MailText)
        }

        if (password.isEmpty()) {
            showError(binding.PasswText, "Password is required")
            return false
        } else {
            clearError(binding.PasswText)
        }

        return true
    }

    private fun showError(editText: EditText, message: String) {
        editText.error = message
    }

    private fun clearError(editText: EditText) {
        editText.error = null
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}

