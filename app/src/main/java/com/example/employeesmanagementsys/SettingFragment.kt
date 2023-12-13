package com.example.employeesmanagementsys

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class SettingFragment : Fragment() {

    private lateinit var updateNameText: EditText
    private lateinit var updateMailText: EditText
    private lateinit var updatePasswordText: EditText
    private lateinit var updateProfileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateNameText = view.findViewById(R.id.updateNameText)
        updateMailText = view.findViewById(R.id.updateMailText)
        updatePasswordText = view.findViewById(R.id.updatePasswordText)
        updateProfileButton = view.findViewById(R.id.updateProfileButton)

        // Retrieve user information and populate UI components
        val sessionManager = SessionManager(requireContext())

        // Set UI components with data from SessionManager
        // Set UI components with data from SessionManager
        val userEmail = sessionManager.getUserEmail()
        val userName = sessionManager.getUserName()
        val userPassword = sessionManager.getUserPassword()

// Ensure that the user data is not blank before setting it to the UI components
        if (userEmail.isNotBlank()) {
            updateMailText.setText(userEmail)
        }

        if (userName.isNotBlank()) {
            updateNameText.setText(userName)
        }

        if (userPassword.isNotBlank()) {
            updatePasswordText.setText(userPassword)
        }


        // Handle profile update button click
        updateProfileButton.setOnClickListener {
            // Obtain the employeeId from the sessionManager
            val employeeId = sessionManager.getUserId()

            if (employeeId.isNotEmpty()) {
                updateProfile(employeeId)
            } else {
                // Handle the case where employeeId is empty or not available
                Toast.makeText(requireContext(), "Error updating profile: Employee ID not available", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateProfile(employeeId: String) {
        val newName = updateNameText.text.toString()
        val newEmail = updateMailText.text.toString()
        val newPassword = updatePasswordText.text.toString()

        val sessionManager = SessionManager(requireContext())

        // Assuming you have an endpoint for updating the profile on the server
        val url = "http://192.168.1.18:3002/employees/$employeeId/update-profile"

        // Update the keys to match the server expectations
        val requestBody = JSONObject().apply {
            put("employee_name", newName)
            put("employee_mail", newEmail)
            put("employee_password", newPassword)
        }


        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, requestBody,
            Response.Listener { response ->
                // Handle the response from the server
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()

                // Update session manager with new user information
                sessionManager.setUserName(newName)
                sessionManager.setUserEmail(newEmail)
                sessionManager.setUserPassword(newPassword)
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(requireContext(), "Error updating profile: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the RequestQueue
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
    }
}
