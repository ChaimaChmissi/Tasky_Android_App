package com.example.employeesmanagementsys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.employeesmanagementsys.databinding.ActivityAddEmployeeBinding
import com.example.employeesmanagementsys.databinding.ActivityUpdateEmployeeBinding

class UpdateEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEmployeeBinding
    private lateinit var db: EmployeesDatabasesHelper
    private var employeeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = EmployeesDatabasesHelper(this)

        employeeId = intent.getIntExtra("employee_id", -1)
        if (employeeId == -1){
            finish()
            return
        }

        val employee = db.getEmployeeByID(employeeId)
        binding.updateNameText.setText(employee.employee_name)
        binding.updateMailText.setText(employee.employee_mail)
        binding.updatePasswText.setText(employee.employee_password)

        binding.updateSaveButton.setOnClickListener{
            val newName = binding.updateNameText.text.toString()
            val newMail = binding.updateMailText.text.toString()
            val newPassword = binding.updatePasswText.text.toString()

            val updatedEmployee = Employee(employeeId, newName, newMail, newPassword)
            db.updateEmployee(updatedEmployee)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}