package com.example.employeesmanagementsys


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeesmanagementsys.databinding.ProjectEmployActivityBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ProjectEmployActivity : AppCompatActivity(), ProjectsEmployAdapter.OnItemClickListener {
    private lateinit var projectsEmployAdapter: ProjectsEmployAdapter
    private val projectRepository = ProjectRepository()
    private lateinit var binding: ProjectEmployActivityBinding

    private lateinit var shareLL : View
    private lateinit var rateLL : View

    private var rotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ProjectEmployActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.projectRecyclerView.layoutManager = LinearLayoutManager(this)
        projectsEmployAdapter = ProjectsEmployAdapter(emptyList(), this)
        binding.projectRecyclerView.adapter = projectsEmployAdapter

        projectsEmployAdapter.setOnItemClickListener(this)

        // Fetch projects from the API using Retrofit
        projectRepository.getProjects(
            onSuccess = { projects ->
                runOnUiThread {
                    projectsEmployAdapter.refreshData(projects)
                }
            },
            onError = { error ->
                Toast.makeText(this, "Error fetching projects: $error", Toast.LENGTH_SHORT).show()
            }
        )




        shareLL = findViewById(R.id.shareLL)
        rateLL = findViewById(R.id.rateLL)
        initShowOut(shareLL)
        initShowOut(rateLL)

        val shareFab = findViewById<FloatingActionButton>(R.id.shareFab)
        val rateFab = findViewById<FloatingActionButton>(R.id.rateFab)
        val settingFab = findViewById<FloatingActionButton>(R.id.settingFab)

        settingFab.setOnClickListener {
            toggleFabMode(it)
        }

        shareFab.setOnClickListener {
            Toast.makeText(this,"Share",Toast.LENGTH_LONG).show()
        }

        rateFab.setOnClickListener {
            Toast.makeText(this,"Rate Us",Toast.LENGTH_LONG).show()
        }
    }

    override fun onItemClick(project: Project) {
        // This method will be called when a project is clicked
        Log.d("ProjectsFragment", "Clicked on project: ${project.project_name}")

        // Navigate to TaskEmployActivity and pass the selected project name
        val intent = Intent(this, TaskEmployActivity::class.java)
        intent.putExtra("selected_project_name", project.project_name.trim())
        startActivity(intent)
    }


    private fun initShowOut(v: View){
        v.apply {
            visibility = View.GONE
            translationY = height.toFloat()
            alpha = 0f
        }
    }
    private fun toggleFabMode(v: View) {
        rotate = rotateFab(v,!rotate)
        if (rotate){
            showIn(shareLL)
            showIn(rateLL)
        }else{
            showOut(shareLL)
            showOut(rateLL)
        }
    }

    private fun showOut(view: View) {
        view.apply {
            visibility = View.VISIBLE
            alpha = 1f
            translationY = 0f
            animate()
                .setDuration(200)
                .translationY(height.toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                        super.onAnimationEnd(animation)
                    }
                })
                .alpha(0f)
                .start()
        }
    }

    private fun showIn(view: View) {
        view.apply {
            visibility = View.VISIBLE
            alpha = 0f
            translationY = height.toFloat()
            animate()
                .setDuration(200)
                .translationY(0f)
                .setListener(object : AnimatorListenerAdapter() {})
                .alpha(1f)
                .start()
        }
    }

    private fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate()
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {})
            .rotation(if (rotate) 180f else 0f)
        return rotate
    }
}