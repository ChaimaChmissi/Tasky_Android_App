package com.example.employeesmanagementsys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation


class ForEmployeeMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee_activity_main)

        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1,"Company",R.drawable.baseline_location_on_24)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2,"Home",R.drawable.ic_home_24)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3,"Profile",R.drawable.ic_settings_24)
        )

        bottomNavigation.setOnClickMenuListener {
            when(it.id){
                1 -> {
                    replaceFragment(NotificationFragment())
                }
                2 -> {
                    replaceFragment(HomeFragment())
                }
                3 -> {
                    replaceFragment(SettingFragment())
                }
            }
        }

        // default Bottom Tab Selected
        replaceFragment(HomeFragment())
        bottomNavigation.show(2)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }
}