package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.FragmentEmployeeBinding


class EmployeeFragment : Fragment() {
private lateinit var binding: FragmentEmployeeBinding
private lateinit var db: EmployeesDatabasesHelper
private lateinit var employeesAdapter: EmployeesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_employee, container, false)
        binding = FragmentEmployeeBinding.inflate(layoutInflater)
        return binding.root


        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = EmployeesDatabasesHelper(requireContext())
        employeesAdapter = EmployeesAdapter(db.getAllEmployees(), requireContext())
        binding.employeeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.employeeRecyclerView.adapter = employeesAdapter


        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddEmployeeActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        employeesAdapter.refreshData(db.getAllEmployees())
    }



}