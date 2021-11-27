package com.kamikaze.yada.diary.writenotes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.kamikaze.yada.R

class ZoomImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YADA)
        setContentView(R.layout.activity_zoom_image)
        val extras = intent.extras
        if (extras != null) {
            val image = intent.getStringExtra("image")
            if (image!=null) {
                val img = findViewById<ImageView>(R.id.zoomimage)
                val imageURI = Uri.parse(image)
                img.setImageURI(null)
                Glide.with(this)
                    .load(imageURI)
                    .into(img);
            }

        }


    }
}