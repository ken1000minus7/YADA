package com.kamikaze.yada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MemorablePlacesActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorable_places);
        ListView listView = findViewById(R.id.listView);
        places.add("Add a new place...");
        locations.add(new LatLng(0, 0));

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("placeNumber", i);
            startActivity(intent);
        });
    }
}