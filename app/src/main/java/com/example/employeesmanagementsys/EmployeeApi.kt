package com.example.employeesmanagementsys

import android.content.Context

class EmployeeApi(context: Context?) {
    private val employeeRepository = EmployeeRepository()

    fun getEmployees(
        successListener: (List<Employee>) -> Unit,
        errorListener: (String) -> Unit
    ) {
        employeeRepository.getEmployees(
            onSuccess = successListener,
            onError = errorListener
        )
    }
}

