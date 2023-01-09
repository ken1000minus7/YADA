package com.kamikaze.yada.pathtracker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.ActivityPathTrackerBinding

class PathTracker : AppCompatActivity() {
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityPathTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.lightblue_light4)
        window.navigationBarColor = resources.getColor(R.color.lightblue_light4)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
