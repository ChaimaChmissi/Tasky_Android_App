package com.example.employeesmanagementsys

import android.content.Context

class ProjectApi(context: Context?) {
    private val projectRepository = ProjectRepository()

    fun getProjects(
        successListener: (List<Project>) -> Unit,
        errorListener: (String) -> Unit
    ) {
        projectRepository.getProjects(
            onSuccess = successListener,
            onError = errorListener
        )
    }
}

