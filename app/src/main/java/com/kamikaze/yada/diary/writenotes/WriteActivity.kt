package com.kamikaze.yada.diary.writenotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.ActivityMainBinding
import com.kamikaze.yada.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWriteBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment2) as NavHostFragment
        navController = navHostFragment.navController
        setContentView(binding.root)




        val position=intent.getIntExtra("position",0)
        Log.d("position", position.toString());

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}