package com.example.employeesmanagementsys


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.FragmentEmployeeBinding
import org.json.JSONArray

class EmployeeFragment : Fragment() {
    private lateinit var binding: FragmentEmployeeBinding
    private lateinit var employeesAdapter: EmployeesAdapter
    private val employeeApi: EmployeeApi by lazy { EmployeeApi(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmployeeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.employeeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        employeesAdapter = EmployeesAdapter(emptyList(), requireContext())
        binding.employeeRecyclerView.adapter = employeesAdapter

        // Fetch employees from the API using Retrofit
        employeeApi.getEmployees(
            { employees ->
                Log.d("API Response", employees.toString())
                requireActivity().runOnUiThread {
                    employeesAdapter.refreshData(employees)
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error fetching employees: $error", Toast.LENGTH_SHORT)
                    .show()
            }
        )

        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddEmployeeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun parseEmployeesFromApiResponse(response: JSONArray?): List<Employee> {
        val employeesList = mutableListOf<Employee>()

        if (response != null) {
            for (i in 0 until response.length()) {
                val employeeJson = response.getJSONObject(i)
                val id = employeeJson.getString("id")
                val name = employeeJson.getString("employee_name")
                val email = employeeJson.getString("employee_mail") // Corrected field name

                val employee = Employee(id, name, email, "")
                employeesList.add(employee)
            }
        }

        return employeesList
    }
}
