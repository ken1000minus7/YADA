package com.kamikaze.yada

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.text.SimpleDateFormat
import java.util.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback, OnMapLongClickListener {
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    private var mMap: GoogleMap? = null
    fun centerMapOnLocation(location: Location?, title: String?) {
        if (location != null) {
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap!!.clear()
            mMap!!.addMarker(MarkerOptions().position(userLocation).title(title))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener!!)
                val lastKnownLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                centerMapOnLocation(lastKnownLocation, "Your Location")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            mMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsActivity, R.raw.map_style_night))
        }
        mMap!!.setOnMapLongClickListener(this)
        val intent = intent
        if (intent.getIntExtra("placeNumber", 0) == 0) {
            // Zoom in on user location
            locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    centerMapOnLocation(location, "Your Location")
                }

                override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
                override fun onProviderEnabled(s: String) {}
                override fun onProviderDisabled(s: String) {}
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener as LocationListener)
                val lastKnownLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                centerMapOnLocation(lastKnownLocation, "Your Location")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } else {
            val placeLocation = Location(LocationManager.GPS_PROVIDER)
            placeLocation.latitude = MemorablePlacesActivity.locations[intent.getIntExtra("placeNumber", 0)].latitude
            placeLocation.longitude = MemorablePlacesActivity.locations[intent.getIntExtra("placeNumber", 0)].longitude
            centerMapOnLocation(placeLocation, MemorablePlacesActivity.places[intent.getIntExtra("placeNumber", 0)])
        }
    }

    override fun onMapLongClick(latLng: LatLng) {
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        var address = ""
        try {
            val listAdddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (listAdddresses != null && listAdddresses.size > 0) {
                if (listAdddresses[0].thoroughfare != null) {
                    if (listAdddresses[0].subThoroughfare != null) {
                        address += listAdddresses[0].subThoroughfare + " "
                    }
                    address += listAdddresses[0].thoroughfare
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (address == "") {
            val sdf = SimpleDateFormat("HH:mm yyyy-MM-dd")
            address += sdf.format(Date())
        }
        mMap!!.addMarker(MarkerOptions().position(latLng).title(address))
        MemorablePlacesActivity.places.add(address)
        MemorablePlacesActivity.locations.add(latLng)
        MemorablePlacesActivity.arrayAdapter?.notifyDataSetChanged()
        Toast.makeText(this, "Location Saved!", Toast.LENGTH_SHORT).show()
    }
}