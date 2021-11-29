package com.kamikaze.yada.webview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.kamikaze.yada.R

class BlogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        val dataset : HashMap<String,String> = hashMapOf("Dan macho1" to " www.danmacho.com",
            "Dan macho2" to " www.danmacho.com" ,"Dan macho3" to " www.danmacho.com"
        ,"Dan macho4" to " www.danmacho.com" ,"Dan macho5" to " www.danmacho.com"
        ,"Dan macho6" to " www.danmacho.com" ,"Dan macho7" to " www.danmacho.com" )


        val datanames: ArrayList<String> = arrayListOf("Dan macho1","Dan macho2","Dan macho3","Dan macho4","Dan macho5","Dan macho6","Dan macho7")
        val recyclerView = findViewById<RecyclerView>(R.id.webrv)
        recyclerView.adapter = WebAdapter(this,dataset,datanames)


    }


}