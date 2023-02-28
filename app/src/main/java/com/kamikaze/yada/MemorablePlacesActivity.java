package com.kamikaze.yada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Objects;

public class MemorablePlacesActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    static String addNewStr = "Add a new place...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorable_places);
        ListView listView = findViewById(R.id.listView);
        if (places.size() == 0)
        {
            places.add(addNewStr);
        } else if (!Objects.equals(places.get(places.size() - 1), addNewStr))
        {
            places.add(addNewStr);
        }
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