package com.example.employeesmanagementsys
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeesmanagementsys.databinding.ActivityAddEmployeeBinding

import androidx.lifecycle.Observer

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEmployeeBinding
    private lateinit var employeeRepository: EmployeeRepository
    private var employeeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        employeeRepository = EmployeeRepository()

        // Observe changes in the employee list
        employeeRepository.employeeListLiveData.observe(this, Observer { employees ->
            // Update your UI or perform any other actions with the updated employee list
            Toast.makeText(this, "Employee list refreshed automatically", Toast.LENGTH_SHORT).show()
        })
        binding.saveButton.setOnClickListener {
            val employeeName = binding.NameText.text.toString()
            val employeeEmail = binding.MailText.text.toString()
            val employeePassword = binding.PasswText.text.toString()

            if (isValidInput(employeeName, employeeEmail, employeePassword)) {
                val employee = Employee(employeeId, employeeName, employeeEmail, employeePassword)

                // Call the addEmployee method in the repository to save the employee
                employeeRepository.addEmployee(
                    employee,
                    onSuccess = {
                        // Employee added successfully
                        Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show()

                        // Navigate back to the employee list fragment
                        onBackPressed()
                    },
                    onError = { errorMessage ->
                        // Handle the error, show a toast, log, or perform other error handling
                        Toast.makeText(this, "Error adding employee: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                )
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