package com.example.employeesmanagementsys

import retrofit2.Call
import retrofit2.http.*

interface TaskApiService {

    @GET("/tasks")
    fun getTasks(): Call<List<Task>>  // Corrected method name

    @POST("/tasks")  // Corrected path for adding a task
    fun addTask(@Body task: Task): Call<Task>

    @GET("/tasks/{taskId}")  // Corrected path for getting a task by ID
    fun getTaskById(@Path("taskId") taskId: String): Call<Task>

    @PUT("/tasks/{taskId}")  // Corrected path for updating a task
    fun updateTask(@Path("taskId") taskId: String, @Body task: Task): Call<Void>

    @DELETE("/tasks/{taskId}")  // Corrected path for deleting a task
    fun deleteTask(@Path("taskId") taskId: String): Call<Void>


    @GET("/tasks")
    fun getTasksByProjectName(@Query("projectName") projectName: String): Call<List<Task>>

    @GET("/tasks/assigned")
    fun getTasksByAssignedTo(@Query("assignedTo") assignedTo: String): Call<List<Task>>

    @PUT("/tasks/{taskId}/status")
    fun updateTaskStatus(@Path("taskId") taskId: String, @Body task: Task): Call<Void>

}


