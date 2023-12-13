package com.example.employeesmanagementsys

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository {

    val apiService: TaskApiService by lazy {
        RetrofitClient.tasksInstance.create(TaskApiService::class.java)
    }

    private val _taskListLiveData = MutableLiveData<List<Task>>().apply { value = emptyList() }
    val taskListLiveData: LiveData<List<Task>> get() = _taskListLiveData

    fun getTasks(onSuccess: (List<Task>) -> Unit, onError: (String) -> Unit) {
        apiService.getTasks().enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _taskListLiveData.value = it  // Update LiveData
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun addTask(task: Task, onSuccess: (Task) -> Unit, onError: (String) -> Unit) {
        apiService.addTask(task).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    // Log additional information for debugging
                    Log.e("TaskRepository", "Error: ${response.code()}")
                    Log.e("TaskRepository", "Error Body: ${response.errorBody()?.string()}")
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                // Log additional information for debugging
                Log.e("TaskRepository", "Network Error: ${t.message}")
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun getTaskById(taskId: String, onSuccess: (Task) -> Unit, onError: (String) -> Unit) {
        apiService.getTaskById(taskId).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                if (response.isSuccessful) {
                    val task = response.body()
                    if (task != null) {
                        onSuccess(task)
                    } else {
                        onError("Error: Null response body")
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun updateTask(
        taskId: String,
        task: Task,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.updateTask(taskId, task).enqueue(object : Callback<Void> {
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

    fun deleteTask(taskId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        apiService.deleteTask(taskId).enqueue(object : Callback<Void> {
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

    fun getTasksByProject(
        projectId: String,
        onSuccess: (List<Task>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Hypothetical method to retrieve tasks based on the project ID
        apiService.getTasksByProjectName(projectId).enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun getTasksByProject(
        project: Project,
        onSuccess: (List<Task>) -> Unit,
        onError: (String) -> Unit
    ) {
        // Hypothetical method to retrieve tasks based on the project ID
        apiService.getTasksByProjectName(project._id).enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun getTasksByProjectName(
        projectName: String,
        onSuccess: (List<Task>) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.getTasksByProjectName(projectName).enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun getTasksByAssignedUser(
        assignedTo: String,
        onSuccess: (List<Task>) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.getTasksByAssignedTo(assignedTo).enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    // Function to update task status
    fun updateTaskStatus(
        taskId: String,
        task: Task,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.updateTaskStatus(taskId, task).enqueue(object : Callback<Void> {
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

