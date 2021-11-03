package com.kamikaze.yada

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

import com.kamikaze.yada.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        setContentView(binding.root)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}