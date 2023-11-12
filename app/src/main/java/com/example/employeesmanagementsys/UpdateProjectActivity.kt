package com.example.employeesmanagementsys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.employeesmanagementsys.databinding.ActivityAddEmployeeBinding
import com.example.employeesmanagementsys.databinding.ActivityUpdateEmployeeBinding
import com.example.employeesmanagementsys.databinding.ActivityUpdateProjectBinding

class UpdateProjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProjectBinding
    private lateinit var db: EmployeesDatabasesHelper
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = EmployeesDatabasesHelper(this)

        projectId = intent.getIntExtra("project_id", -1)
        if (projectId == -1){
            finish()
            return
        }

        val project = db.getProjectByID(projectId)
        binding.updateNameText.setText(project.project_name)
        binding.updateDateText.setText(project.project_date)


        binding.updateSaveButton.setOnClickListener{
            val newName = binding.updateNameText.text.toString()
            val newDate = binding.updateDateText.text.toString()


            val updatedProject = Project(projectId, newName, newDate)
            db.updateProject(updatedProject)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }
    }
}