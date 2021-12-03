package com.kamikaze.yada.pathtracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.ActivityPathTrackerBinding

class PathTracker : AppCompatActivity() {
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityPathTrackerBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentPathTracker) as NavHostFragment
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.lightblue_light4)
        window.navigationBarColor= resources.getColor(R.color.lightblue_light4)

    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    }
