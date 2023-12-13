package com.example.employeesmanagementsys

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectRepository {

    val apiService: ProjectApiService by lazy {
        RetrofitClient.projectsInstance.create(ProjectApiService::class.java)
    }

    private val _projectListLiveData = MutableLiveData<List<Project>>().apply { value = emptyList() }
    val projectListLiveData: LiveData<List<Project>> get() = _projectListLiveData

    fun getProjects(onSuccess: (List<Project>) -> Unit, onError: (String) -> Unit) {
        apiService.getProjects().enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _projectListLiveData.value = it  // Update LiveData
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun addProject(project: Project, onSuccess: (Project) -> Unit, onError: (String) -> Unit) {
        apiService.addProject(project).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun getProjectById(projectId: String, onSuccess: (Project) -> Unit, onError: (String) -> Unit) {
        apiService.getProjectById(projectId).enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    val project = response.body()
                    if (project != null) {
                        onSuccess(project)
                    } else {
                        onError("Error: Null response body")
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun updateProject(
        projectId: String,
        project: Project,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.updateProject(projectId, project).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun deleteProject(projectId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        apiService.deleteProject(projectId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

}
