package com.example.employeesmanagementsys


import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.example.employeesmanagementsys.databinding.ActivityAddProjectBinding
import java.util.Calendar

class AddProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProjectBinding
    private lateinit var projectRepository: ProjectRepository

    private val CHANNEL_ID = "myChannelId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectRepository = ProjectRepository()

        // Observe changes in the project list
        projectRepository.projectListLiveData.observe(this, Observer { projects ->
            // Update your UI or perform any other actions with the updated project list
            Toast.makeText(this, "Project list refreshed automatically", Toast.LENGTH_SHORT).show()
        })

        binding.saveButton.setOnClickListener {
            val projectName = binding.NameText.text.toString()
            val projectDate = binding.DateText.text.toString()

            if (isValidInput(projectName, projectDate)) {
                val project = Project("", projectName, projectDate)

                // Call the addProject method in the repository to save the project
                projectRepository.addProject(
                    project,
                    onSuccess = {
                        // Project added successfully
                        Toast.makeText(this, "Project added successfully", Toast.LENGTH_SHORT).show()

                        // Create and show notification
                        showNotification()

                        // Finish the current activity to go back to the previous one (ProjectFragment)
                        finish()
                    },
                    onError = { errorMessage ->
                        // Handle the error, show a toast, log, or perform other error handling
                        Toast.makeText(this, "Error adding project: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun isValidInput(name: String, date: String): Boolean {
        if (name.isEmpty()) {
            showError(binding.NameText, "Name is required")
            return false
        } else {
            clearError(binding.NameText)
        }

        if (date.isEmpty()) {
            showError(binding.DateText, "Date is required")
            return false
        } else {
            clearError(binding.DateText)
        }

        return true
    }

    private fun showError(editText: EditText, message: String) {
        editText.error = message
    }

    private fun clearError(editText: EditText) {
        editText.error = null
    }

    // Function to show date picker dialog
    fun showDatePickerDialog(view: View) {
        val editTextDate = findViewById<EditText>(R.id.DateText)
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                editTextDate.setText(selectedDate)
            },
            year,
            month,
            day
        )

        // Set the minimum date (optional)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        datePickerDialog.show()
    }

    // Function to create and show a basic notification
    private fun showNotification() {
        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Project Added")
            .setContentText("Your project has been added successfully!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
    }

    // Function to create the notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Notification Channel"
            val descriptionText = "Channel for project notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}