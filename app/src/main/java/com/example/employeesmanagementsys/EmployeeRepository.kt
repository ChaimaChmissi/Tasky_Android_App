package com.example.employeesmanagementsys


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmployeeRepository {


    val apiService: EmployeeApiService by lazy {
        RetrofitClient.employeesInstance.create(EmployeeApiService::class.java)
    }



    private val _employeeListLiveData = MutableLiveData<List<Employee>>().apply { value = emptyList() }
    val employeeListLiveData: LiveData<List<Employee>> get() = _employeeListLiveData

    fun getEmployees(onSuccess: (List<Employee>) -> Unit, onError: (String) -> Unit) {
        apiService.getEmployees().enqueue(object : Callback<List<Employee>> {
            override fun onResponse(call: Call<List<Employee>>, response: Response<List<Employee>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _employeeListLiveData.value = it  // Update LiveData
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Employee>>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }
    fun addEmployee(employee: Employee, onSuccess: (Employee) -> Unit, onError: (String) -> Unit) {
        apiService.addEmployee(employee).enqueue(object : Callback<Employee> {
            override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Employee>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }

    fun getEmployeeById(employeeId: String, onSuccess: (Employee) -> Unit, onError: (String) -> Unit) {
        apiService.getEmployeeById(employeeId).enqueue(object : Callback<Employee> {
            override fun onResponse(call: Call<Employee>, response: Response<Employee>) {
                if (response.isSuccessful) {
                    val employee = response.body()
                    if (employee != null) {
                        onSuccess(employee)
                    } else {
                        onError("Error: Null response body")
                    }
                } else {
                    onError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Employee>, t: Throwable) {
                onError("Network Error: ${t.message}")
            }
        })
    }



    fun updateEmployee(
        employeeId: String,
        employee: Employee,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.updateEmployee(employeeId, employee).enqueue(object : Callback<Void> {
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


    fun deleteEmployee(employeeId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        apiService.deleteEmployee(employeeId).enqueue(object : Callback<Void> {
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
