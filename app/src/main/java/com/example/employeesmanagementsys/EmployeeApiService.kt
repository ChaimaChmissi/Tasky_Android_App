package com.example.employeesmanagementsys

import retrofit2.Call
import retrofit2.http.*


interface EmployeeApiService {

    @GET("/employees")
    fun getEmployees(): Call<List<Employee>>


    @POST("/employees")
    fun addEmployee(@Body employee: Employee): Call<Employee>

    @GET("/employees/{employeeId}")
    fun getEmployeeById(@Path("employeeId") employeeId: String): Call<Employee>
    @PUT("/employees/{employeeId}")
    fun updateEmployee(
        @Path("employeeId") employeeId: String,
        @Body employee: Employee
    ): Call<Employee>
    @PUT("/employees/{employeeId}")
    fun updateEmployee(@Path("employeeId") employeeId: Int, @Body employee: Employee?): Call<Void?>?
    @DELETE("/employees/{employeeId}")
    fun deleteEmployee(@Path("employeeId") employeeId: Int): Call<Void>
}

