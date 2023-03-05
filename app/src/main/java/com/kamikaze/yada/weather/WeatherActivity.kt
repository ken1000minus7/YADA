package com.kamikaze.yada.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kamikaze.yada.R
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.math.abs

class WeatherActivity : AppCompatActivity() {
    private val url = "http://api.openweathermap.org/data/2.5/weather"
    private val appid = "cd0755a868710f3da85d3e63a838080e"
    var imgUrl = "http://openweathermap.org/img/wn/"
    var celcius = "°C"
    var df = DecimalFormat("#.##")
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this@WeatherActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@WeatherActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@WeatherActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            weatherDetails
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                weatherDetails
            }
        }
    }

    @get:SuppressLint("MissingPermission")
    val weatherDetails: Unit
        get() {
            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    setData(location)
                }

                override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
                override fun onProviderEnabled(s: String) {}
                override fun onProviderDisabled(s: String) {}
            }
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                10f,
                locationListener
            )
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                10f,
                locationListener
            )
            var location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location == null) location =
                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            setData(location)
        }

    private fun setData(userLocation: Location?) {
        if (userLocation == null) {
            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            return
        }
        val latitude = userLocation.latitude
        val longitude = userLocation.longitude
        val tempUrl = "$url?lat=$latitude&lon=$longitude&appid=$appid"
        val stringRequest = StringRequest(Request.Method.GET, tempUrl, { response ->
            try {
                val jsonResponse = JSONObject(response)
                val jsonArray = jsonResponse.getJSONArray("weather")
                val jsonObjectWeather = jsonArray.getJSONObject(0)
                val main = jsonObjectWeather.getString("main")
                var description = jsonObjectWeather.getString("description")
                description = description.substring(0, 1)
                    .uppercase(Locale.getDefault()) + description.substring(1)
                val jsonObjectMain = jsonResponse.getJSONObject("main")
                val temp = jsonObjectMain.getDouble("temp") - 273.15
                val minTemp = jsonObjectMain.getDouble("temp_min") - 273.15
                val maxTemp = jsonObjectMain.getDouble("temp_max") - 273.15
                val pressure = jsonObjectMain.getInt("pressure").toFloat()
                val humidity = jsonObjectMain.getInt("humidity")
                val jsonObjectWind = jsonResponse.getJSONObject("wind")
                val jsonObjectClouds = jsonResponse.getJSONObject("clouds")
                var cityName = jsonResponse.getString("name")
                if (cityName == "") cityName = "Unknown city"
                var countryName: String? = ""
                if (jsonResponse.getJSONObject("sys").has("country")) countryName =
                    jsonResponse.getJSONObject("sys").getString("country")
                val iconUrl = imgUrl + jsonObjectWeather.getString("icon") + "@2x.png"
                val timestamp = jsonResponse.getInt("dt")
                val date = Date(1000L * timestamp)
                var timezone = "GMT"
                timezone += if (date.timezoneOffset < 0) "+" else "-"
                val offset = abs(date.timezoneOffset)
                var mins = (offset % 60).toString()
                if (mins.length == 1) mins = "0$mins"
                timezone += (offset / 60).toString() + ":" + mins
                var minutes = date.minutes.toString()
                if (minutes.length == 1) minutes = "0$minutes"
                var hours = date.hours.toString()
                if (hours.length == 1) hours = "0$hours"
                val tempText = findViewById<View>(R.id.temperature) as TextView
                val tempMinmax = findViewById<View>(R.id.temperature_minmax) as TextView
                val weatherMain = findViewById<View>(R.id.weather_main) as TextView
                val weatherDescription = findViewById<View>(R.id.weather_description) as TextView
                val cityText = findViewById<View>(R.id.city) as TextView
                val countryText = findViewById<View>(R.id.country) as TextView
                val dateText = findViewById<View>(R.id.date) as TextView
                val timeText = findViewById<View>(R.id.timestamp) as TextView
                val dayText = findViewById<View>(R.id.day) as TextView
                val humidityText = findViewById<View>(R.id.humidity) as TextView
                val pressureText = findViewById<View>(R.id.pressure) as TextView
                val timezoneText = findViewById<View>(R.id.timezone) as TextView
                val weatherImg = findViewById<View>(R.id.weather_image) as ImageView
                tempText.text = buildString {
                    append(String.format("%.0f", temp))
                    append(celcius)
                }
                tempMinmax.text = buildString {
                    append(String.format("%.0f", minTemp))
                    append(celcius)
                    append("/")
                    append(String.format("%.0f", maxTemp))
                    append(celcius)
                }
                weatherMain.text = main
                weatherDescription.text = description
                cityText.text = cityName
                countryText.text = countryName
                dateText.text = buildString {
                    append(getMonth(date.month + 1))
                    append(" ")
                    append(date.date)
                    append(", ")
                    append((date.year + 1900))
                }
                timeText.text = buildString {
                    append(hours)
                    append(":")
                    append(minutes)
                }
                dayText.text = getDay(date.day)
                humidityText.text = buildString {
                    append(humidity)
                    append("%")
                }
                pressureText.text = buildString {
                    append(String.format("%.0f", pressure))
                    append("hPa")
                }
                timezoneText.text = timezone
                Picasso.get().load(iconUrl).into(weatherImg)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error: VolleyError? ->
            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
        }
        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(stringRequest)
    }

    private fun getMonth(n: Int): String {
        when (n) {
            1 -> return "January"
            2 -> return "February"
            3 -> return "March"
            4 -> return "April"
            5 -> return "May"
            6 -> return "June"
            7 -> return "July"
            8 -> return "August"
            9 -> return "September"
            10 -> return "October"
            11 -> return "November"
            12 -> return "December"
        }
        return ""
    }

    private fun getDay(n: Int): String {
        when (n) {
            1 -> return "Monday"
            2 -> return "Tuesday"
            3 -> return "Wednesday"
            4 -> return "Thursday"
            5 -> return "Friday"
            6 -> return "Saturday"
            0 -> return "Sunday"
        }
        return ""
    }
}