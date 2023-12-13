package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.employeesmanagementsys.SessionManager
import org.json.JSONException

class login : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        progressBar = findViewById(R.id.progressBar)
        sessionManager = SessionManager(this)

        val loginButton: Button = findViewById(R.id.getStartedButton)
        loginButton.setOnClickListener {
            loginUser()
        }

        // Check if the user is already logged in
        if (sessionManager.isLoggedIn()) {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        val userRole = sessionManager.getUserRole()
        Log.d("LoginActivity", "User role: $userRole")

        val intent = if (userRole == "admin") {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, ForEmployeeMainActivity::class.java)
        }

        startActivity(intent)
        finish()
    }


    private fun loginUser() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        val url = "http://192.168.1.18:3002/employees"

        progressBar.visibility = View.VISIBLE

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "$url?employee_mail=$email", null,
            Response.Listener { response ->
                progressBar.visibility = View.GONE

                // Handle the response from the server
                try {
                    for (i in 0 until response.length()) {
                        val employee = response.getJSONObject(i)
                        val employeeEmail = employee.getString("employee_mail")
                        val employeePassword = employee.getString("employee_password")

                        if (email == employeeEmail && password == employeePassword) {
                            // Login successful
                            sessionManager.setLoggedIn(true)
                            sessionManager.setUserName(employee.getString("employee_name"))
                            sessionManager.setUserEmail(employeeEmail)
                            sessionManager.setUserPassword(employeePassword)
                            val retrievedEmployeeId = employee.getString("_id")
                            sessionManager.setUserId(retrievedEmployeeId)
                            Log.d("LoginActivity", "Retrieved employeeId: $retrievedEmployeeId")
                            val employeeRole = employee.getString("employee_role")
                            sessionManager.setUserRole(employeeRole)
                            startMainActivity()
                            return@Listener
                        }
                    }

                    // Login failed
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                } catch (e: JSONException) {
                    Log.e("LoginActivity", "JSON parsing error: $e")
                    Toast.makeText(this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                progressBar.visibility = View.GONE
                Log.e("LoginActivity", "Volley error: $error")
                Toast.makeText(this, "Error occurred. Please try again.", Toast.LENGTH_SHORT).show()
            })

        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }
}