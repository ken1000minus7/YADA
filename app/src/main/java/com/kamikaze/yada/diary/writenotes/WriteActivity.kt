package com.kamikaze.yada.diary.writenotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.kamikaze.yada.R
import com.kamikaze.yada.R.*
import com.kamikaze.yada.databinding.ActivityMainBinding
import com.kamikaze.yada.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {
    private lateinit var navController: NavController
   public var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWriteBinding.inflate(layoutInflater)
        val navHostFragment = supportFragmentManager.findFragmentById(id.myNavHostFragment2) as NavHostFragment
        navController = navHostFragment.navController
        position=intent.getIntExtra("position",0)
        Log.d("position", position.toString());
        setContentView(binding.root)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onBackPressed() {

        val et = findViewById<EditText>(R.id.edithere)
        val tv = findViewById<TextView>(R.id.seehere)
        if (et.isVisible){
            et.visibility = View.GONE
            tv.visibility = View.VISIBLE
        }
        else{
            super.onBackPressed()
        }

    }


}