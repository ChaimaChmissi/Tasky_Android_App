package com.example.employeesmanagementsys

import android.content.Context

class TaskApi(context: Context?) {
    private val taskRepository = TaskRepository()

    fun getTasks(
        successListener: (List<Task>) -> Unit,
        errorListener: (String) -> Unit
    ) {
        taskRepository.getTasks(
            onSuccess = successListener,
            onError = errorListener
        )
    }
}

