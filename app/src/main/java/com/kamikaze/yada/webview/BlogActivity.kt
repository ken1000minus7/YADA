package com.kamikaze.yada.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.kamikaze.yada.R

class BlogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        val dataset : HashMap<String,String> = hashMapOf("Rough Guides" to "https://www.roughguides.com/india/",
            "TimesTravel" to "https://timesofindia.indiatimes.com/travel/guides" ,"Nomadic Matt" to "https://www.nomadicmatt.com/travel-guides/"
        ,"Travel and Leisure" to "https://www.travelandleisure.com/travel-guide" ,"Lonely Planet" to "https://www.lonelyplanet.com/"
        ,"Yatra" to "https://www.yatra.com/india-tourism" ,"WikiTravel" to "https://wikitravel.org/" )


        val datanames: ArrayList<String> = arrayListOf("Rough Guides","TimesTravel","Nomadic Matt","Travel and Leisure","Lonely Planet","Yatra","WikiTravel")
        val recyclerView = findViewById<RecyclerView>(R.id.webrv)
        recyclerView.adapter = WebAdapter(this,dataset,datanames)
    }
}