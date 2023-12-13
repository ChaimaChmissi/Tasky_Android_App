package com.example.employeesmanagementsys

data class Task(
    val _id: String,
    val task_title: String,
    val task_description: String,
    var task_status: String,
    val assignedTo: String,
    val projectName: String,
)
