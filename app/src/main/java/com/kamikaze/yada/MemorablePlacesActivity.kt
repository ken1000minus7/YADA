package com.kamikaze.yada

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng

class MemorablePlacesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memorable_places)
        val listView = findViewById<ListView>(R.id.listView)
        if (places.size == 0) {
            places.add(addNewStr)
        } else if (places[places.size - 1] != addNewStr) {
            places.add(addNewStr)
        }
        locations.add(LatLng(0.0, 0.0))
        arrayAdapter = ArrayAdapter<Any?>(this, android.R.layout.simple_list_item_1, places as List<Any?>)
        listView.adapter = arrayAdapter
        listView.onItemClickListener = OnItemClickListener { adapterView: AdapterView<*>?, view: View?, i: Int, l: Long ->
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("placeNumber", i)
            startActivity(intent)
        }
    }

    companion object {
        var places = ArrayList<String?>()
        var locations = ArrayList<LatLng>()
        var arrayAdapter: ArrayAdapter<*>? = null
        var addNewStr = "Add a new place..."
    }
}