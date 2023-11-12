package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.employeesmanagementsys.R

class GetStarted_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_started_1)

        val getStartedButton = findViewById<View>(R.id.getStartedButton)

        // Set an OnClickListener for the button
        getStartedButton.setOnClickListener {
            // Define the navigation logic to go to the GetStarted_2 activity
            val intent = Intent(this, GetStarted_2::class.java)
            startActivity(intent)
        }
    }
}