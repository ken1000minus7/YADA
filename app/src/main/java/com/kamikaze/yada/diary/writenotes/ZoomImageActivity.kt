package com.kamikaze.yada.diary.writenotes

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.net.toUri
import com.kamikaze.yada.R

class ZoomImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_image)
        val extras = intent.extras
        if (extras!=null){
            val image = intent.getStringExtra("image")
            val img = findViewById<ImageView>(R.id.zoomimage)
            val imageURI = Uri.parse(image)
            img.setImageURI(null);

            img.setImageURI(imageURI)
        }

       else{
        Log.d("bhaiii", "is it working ")}



    }
}