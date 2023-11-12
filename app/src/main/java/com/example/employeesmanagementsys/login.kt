package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.employeesmanagementsys.EmployeeFragment


class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //    setContentView(R.layout.login)

        setContentView(R.layout.login)

        val getStartedButton = findViewById<View>(R.id.getStartedButton)

        getStartedButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) // Start the MainActivity
            startActivity(intent)
        }
    }}
