package com.kamikaze.yada.webview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kamikaze.yada.R

class BlogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        val btn1 = findViewById<Button>(R.id.random2)
        val btn2 = findViewById<Button>(R.id.random1)

        btn1.setOnClickListener{
            val intent = Intent(this , WebActivity::class.java)
            intent.putExtra("url" ,"https://mapify.travel/destinations/India/" )
            startActivity(intent)
        }
        btn2.setOnClickListener{
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("url" , "https://the-shooting-star.com/")
            startActivity(intent)
        }
    }


}