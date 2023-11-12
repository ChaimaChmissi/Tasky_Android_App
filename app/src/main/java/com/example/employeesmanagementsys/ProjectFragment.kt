package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.FragmentEmployeeBinding
import com.example.employeesmanagementsys.databinding.FragmentProjectBinding


class ProjectFragment : Fragment() {
    private lateinit var binding: FragmentProjectBinding
    private lateinit var db: EmployeesDatabasesHelper
    private lateinit var projectsAdapter: ProjectsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_employee, container, false)
        binding = FragmentProjectBinding.inflate(layoutInflater)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = EmployeesDatabasesHelper(requireContext())
        projectsAdapter = ProjectsAdapter(db.getAllProjects(), requireContext())
        binding.projectRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.projectRecyclerView.adapter = projectsAdapter


        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(), AddProjectActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        projectsAdapter.refreshData(db.getAllProjects())
    }



}