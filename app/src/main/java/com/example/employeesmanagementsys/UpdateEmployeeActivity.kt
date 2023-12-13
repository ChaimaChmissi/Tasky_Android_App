package com.example.employeesmanagementsys

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.employeesmanagementsys.databinding.ActivityUpdateEmployeeBinding

class UpdateEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEmployeeBinding
    private lateinit var employeeRepository: EmployeeRepository
    private var employeeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize your EmployeeRepository
        employeeRepository = EmployeeRepository()  // Make sure this is correctly initialized

        // Retrieve the employee ID from the intent
        employeeId = intent.getStringExtra("employee_id") ?: ""

        // Set up a click listener for the update button
        binding.updateButton.setOnClickListener {
            updateEmployee()
        }

        // Fetch employee data using the employee ID and populate the UI
        fetchEmployeeData()

        // Observe changes in the employee list
        employeeRepository.employeeListLiveData.observe(this, Observer { employees ->
            // Update your UI or perform any other actions with the updated employee list
            Toast.makeText(this, "Employee list refreshed automatically", Toast.LENGTH_SHORT).show()
        })
    }

    private fun fetchEmployeeData() {
        // Implement the logic to fetch employee data using the employee ID
        employeeRepository.getEmployeeById(
            employeeId,
            onSuccess = { employee ->
                // Check if the employee is not null before populating the UI
                if (employee != null) {
                    // Populate the UI with the fetched employee data
                    binding.updateNameText.setText(employee.employee_name)
                    binding.updateMailText.setText(employee.employee_mail)
                    binding.updatePasswordText.setText(employee.employee_password)

                    // Add logic for other fields as needed
                } else {
                    // Handle the case where the employee is null
                    Toast.makeText(
                        this,
                        "Error: Employee not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
        val updatedPassword = binding.updatePasswordText.text.toString()

        // Validate the input
        if (isValidInput(updatedName, updatedEmail)) {
            val updatedEmployee = Employee(employeeId, updatedName, updatedEmail, updatedPassword)

            // Call the updateEmployee method in the repository to update the employee
            employeeRepository.updateEmployee(
                employeeId,
                updatedEmployee,
                onSuccess = {
                    // Employee updated successfully
                    Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show()

                    // Trigger a refresh of the employee list

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
