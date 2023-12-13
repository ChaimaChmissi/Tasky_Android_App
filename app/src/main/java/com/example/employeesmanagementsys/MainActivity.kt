
// MainActivity.kt
package com.example.employeesmanagementsys

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawLayout: DrawerLayout
    private lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        if (!sessionManager.isLoggedIn()) {
            val intent = Intent(this, login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        } else {
            val userEmail = sessionManager.getUserEmail()
            val profileFragment = ProfileFragment()
            startMainActivity(profileFragment, userEmail)
        }
    }


    private fun startMainActivity(fragment: Fragment, userEmail: String) {
        drawLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Display the ProfileFragment initially
        if (fragment is ProfileFragment) {
            fragment.arguments = Bundle().apply {
                putString("userEmail", userEmail)
            }
            replaceFragment(fragment)
        } else {
            replaceFragment(fragment)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> replaceFragment(ProfileFragment())
            R.id.nav_project -> replaceFragment(ProjectFragment())
            R.id.nav_dashbord -> replaceFragment(DashboardAdminFragment())
            R.id.nav_employee -> replaceFragment(EmployeeFragment())
            R.id.nav_logout -> logout()
        }
        drawLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun logout() {
        // Clear the session and go back to the login activity
        sessionManager.setLoggedIn(false)
        val intent = Intent(this, login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (drawLayout.isDrawerOpen(GravityCompat.START)) {
            drawLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
