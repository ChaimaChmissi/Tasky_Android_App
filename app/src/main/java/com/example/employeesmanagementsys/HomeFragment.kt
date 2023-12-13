package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class HomeFragment : Fragment() {
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sessionManager = SessionManager(requireContext()) // Initialize sessionManager

        val addButton: View = view.findViewById(R.id.project)
        addButton.setOnClickListener {
            val intent = Intent(requireActivity(), TaskEmployActivity::class.java)
            startActivity(intent)
        }

        // Move the logout function here
        val logoutButton: View = view.findViewById(R.id.textView10)
        logoutButton.setOnClickListener {
            logout()
        }

        return view
    }

    private fun logout() {
        // Clear the session and go back to the login activity
        sessionManager.setLoggedIn(false)
        val intent = Intent(requireContext(), login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

}