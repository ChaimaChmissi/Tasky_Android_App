package com.example.employeesmanagementsys

import retrofit2.Call
import retrofit2.http.*


interface ProjectApiService {

    @GET("/projects")
    fun getProjects(): Call<List<Project>>


    @POST("/projects")
    fun addProject(@Body project: Project): Call<Project>
    @GET("/projects/{projectId}")
    fun getProjectById(@Path("projectId") projectId: String): Call<Project>

    @PUT("/projects/{projectId}")
    fun updateProject(@Path("projectId") projectId: String, @Body project: Project): Call<Void>

    @DELETE("/projects/{projectId}")
    fun deleteProject(@Path("projectId") projectId: String): Call<Void>

}
