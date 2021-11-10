package com.kamikaze.yada

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamikaze.yada.auth.Check
import com.kamikaze.yada.auth.LoginActivity
import com.kamikaze.yada.dao.UserDao

import com.kamikaze.yada.databinding.ActivityMainBinding
import com.kamikaze.yada.model.User


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        auth = Firebase.auth

        setContentView(binding.root)
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)

    }
    private fun updateUI(firebaseuser: FirebaseUser?) {
        //Navigate to MainActivity
        if (firebaseuser == null){
            Log.w(LoginActivity.TAG, "go next page")
            return
        }
        startActivity(Intent(this, MainPageActivity::class.java))
        finish()
    }
}