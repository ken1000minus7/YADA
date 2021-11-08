package com.kamikaze.yada.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.kamikaze.yada.R

class Check : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        val userID = intent.getStringExtra("username")
        val emailID = intent.getStringExtra("email-id")
        val tv_user: TextView = findViewById(R.id.userid)
        val tv_email: TextView = findViewById(R.id.email)
        tv_user.text = "$userID"
        tv_email.text = "$emailID"

    }


}

