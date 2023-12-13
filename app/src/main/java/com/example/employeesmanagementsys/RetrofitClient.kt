package com.example.employeesmanagementsys

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {
    private const val BASE_URL_EMPLOYEES = "http://192.168.1.18:3002/"
    private const val BASE_URL_PROJECTS = "http://192.168.1.18:3003/"
    private const val BASE_URL_TASKS = "http://192.168.1.18:3004/"

    val employeesInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_EMPLOYEES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val projectsInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PROJECTS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val tasksInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_TASKS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
