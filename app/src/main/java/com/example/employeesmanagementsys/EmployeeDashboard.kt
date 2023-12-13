package com.example.employeesmanagementsys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler


class EmployeeDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_dashboard)

        // Delay for 2 seconds and then navigate to another activity
        Handler().postDelayed({
            val intent = Intent(this, GetStarted_1::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2000 milliseconds (2 seconds)
    }
}
