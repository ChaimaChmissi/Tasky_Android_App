// UpdateEmployeeActivity.kt

package com.example.employeesmanagementsys

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.employeesmanagementsys.databinding.ActivityUpdateEmployeeBinding
// UpdateEmployeeActivity.kt

class UpdateEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEmployeeBinding
    private lateinit var employeeRepository: EmployeeRepository
    private var employeeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize your EmployeeRepository
        employeeRepository = EmployeeRepository()

        // Retrieve the employee ID from the intent
        employeeId = intent.getStringExtra("employee_id") ?: ""

        // Fetch employee data using the employee ID and populate the UI
        fetchEmployeeData {
            // Set up a click listener for the update button
            binding.updateSaveButton.setOnClickListener {
                updateEmployee()
            }
        }
        // Set up a click listener for the update button
        binding.updateSaveButton.setOnClickListener {
            updateEmployee()
        }
    }


    private fun fetchEmployeeData(callback: () -> Unit) {
        // Implement the logic to fetch employee data using the employee ID
        employeeRepository.getEmployeeById(
            employeeId,
            onSuccess = { employee ->
                // Populate the UI with the fetched employee data
                binding.updateNameText.setText(employee.employee_name)
                binding.updateMailText.setText(employee.employee_mail)
                // Add logic for other fields as needed

                // Call the callback to indicate that the data has been fetched
                callback()
            },
            onError = { errorMessage ->
                // Handle the error, show a toast, log, or perform other error handling
                Toast.makeText(
                    this,
                    "Error fetching employee data: $errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun updateEmployee() {
        // Retrieve updated data from UI fields
        val updatedName = binding.updateNameText.text.toString()
        val updatedEmail = binding.updateMailText.text.toString()
        // Retrieve other updated fields as needed

        // Validate the input
        if (isValidInput(updatedName, updatedEmail)) {
            val updatedEmployee = Employee(employeeId, updatedName, updatedEmail, "")

            // Call the updateEmployee method in the repository to update the employee
            employeeRepository.updateEmployee(
                employeeId,
                updatedEmployee,
                onSuccess = {
                    // Employee updated successfully

                    Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show()
                    finish() // Finish the current activity after updating
                },
                onError = { errorMessage ->
                    // Handle the error, show a toast, log, or perform other error handling
                    Toast.makeText(this, "Error updating employee: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }


    private fun isValidInput(name: String, email: String): Boolean {
        // Implement your validation logic here
        // Return true if the input is valid, false otherwise
        return true
    }
}
