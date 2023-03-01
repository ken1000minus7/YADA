package com.kamikaze.yada

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class HikersWatchActivity : AppCompatActivity() {
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = resources.getColor(R.color.lightblue_light4)
        setContentView(R.layout.activity_hikers_watch)
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location: Location -> updateLocationInfo(location) }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening()
        }
    }

    private fun startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener!!)
        }
    }

    private fun updateLocationInfo(location: Location) {
        val latTextView = findViewById<TextView>(R.id.latTextView)
        val longTextView = findViewById<TextView>(R.id.longTextView)
        val accTextView = findViewById<TextView>(R.id.accTextView)
        val altTextView = findViewById<TextView>(R.id.altTextView)
        val addressTextView = findViewById<TextView>(R.id.addressTextView)
        latTextView.text = buildString {
            append("Latitude: ")
            append(location.latitude)
        }
        longTextView.text = buildString {
            append("Longitude: ")
            append(location.longitude)
        }
        accTextView.text = buildString {
            append("Accuracy: ")
            append(location.accuracy)
        }
        altTextView.text = buildString {
            append("Altitude: ")
            append(location.altitude)
        }
        var address = "Could not find address :("
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val listAddresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (listAddresses != null && listAddresses.size > 0) {
                address = "Address : \n"
                if (listAddresses[0].thoroughfare != null) {
                    address += """
                        ${listAddresses[0].thoroughfare}
                        
                        """.trimIndent()
                }
                if (listAddresses[0].locality != null) {
                    address += """
                        ${listAddresses[0].locality}
                        
                        """.trimIndent()
                }
                if (listAddresses[0].postalCode != null) {
                    address += """
                        ${listAddresses[0].postalCode}
                        
                        """.trimIndent()
                }
                if (listAddresses[0].adminArea != null) {
                    address += """
                        ${listAddresses[0].adminArea}
                        
                        """.trimIndent()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        addressTextView.text = address
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}