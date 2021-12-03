package com.kamikaze.yada.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kamikaze.yada.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Date;

public class WeatherActivity extends AppCompatActivity {


//    EditText etCity, etCountry;
//    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "cd0755a868710f3da85d3e63a838080e";
    String imgUrl="http://openweathermap.org/img/wn/";
    String celcius="°C";
    DecimalFormat df = new DecimalFormat("#.##");
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else
        {
            getWeatherDetails();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherDetails();
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void getWeatherDetails() {
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                setData(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,10,locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,locationListener);
        Location location= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(location==null) location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setData(location);

    }

    private void setData(Location userLocation)
    {
        if(userLocation==null)
        {
            Log.d("what","location null ig");

            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        double latitude=userLocation.getLatitude();
        double longitude= userLocation.getLongitude();
        Log.d("location",latitude+" "+longitude);
        String tempUrl=url+"?lat="+latitude+"&lon="+longitude+"&appid="+appid;
        Log.d("url",tempUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String main=jsonObjectWeather.getString("main");
                    String description = jsonObjectWeather.getString("description");
                    description=description.substring(0,1).toUpperCase()+description.substring(1);
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp =jsonObjectMain.getDouble("temp") - 273.15 ;
                    double minTemp=jsonObjectMain.getDouble("temp_min")-273.15;
                    double maxTemp=jsonObjectMain.getDouble("temp_max")-273.15;
                    //          double feelsLike = jsonObjectMain.getDouble("feels Like ") -273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String cityName = jsonResponse.getString("name");
                    if(cityName.equals("")) cityName="Unknown city";
                    String countryName="";
                    if(jsonResponse.getJSONObject("sys").has("country")) countryName=jsonResponse.getJSONObject("sys").getString("country");
                    String iconUrl=imgUrl+jsonObjectWeather.getString("icon")+"@2x.png";
                    int timestamp=jsonResponse.getInt("dt");
                    Date date=new Date((long)1000*timestamp);
                    String timezone="GMT";
                    if(date.getTimezoneOffset()<0) timezone+="+";
                    else timezone+="-";
                    int offset=Math.abs(date.getTimezoneOffset());
                    String mins=String.valueOf(offset%60);
                    if(mins.length()==1) mins="0"+mins;
                    timezone+=(offset/60)+":"+mins;
                    String minutes=String.valueOf(date.getMinutes());
                    if(minutes.length()==1) minutes="0"+minutes;
                    String hours=String.valueOf(date.getHours());
                    if(hours.length()==1) hours="0"+hours;

                    TextView tempText=(TextView) findViewById(R.id.temperature);
                    TextView tempMinmax=(TextView) findViewById(R.id.temperature_minmax);
                    TextView weatherMain=(TextView) findViewById(R.id.weather_main);
                    TextView weatherDescription=(TextView) findViewById(R.id.weather_description);
                    TextView cityText=(TextView) findViewById(R.id.city);
                    TextView countryText=(TextView) findViewById(R.id.country);
                    TextView dateText=(TextView) findViewById(R.id.date);
                    TextView timeText=(TextView) findViewById(R.id.timestamp);
                    TextView dayText=(TextView) findViewById(R.id.day);
                    TextView humidityText=(TextView) findViewById(R.id.humidity);
                    TextView pressureText=(TextView) findViewById(R.id.pressure);
                    TextView timezoneText=(TextView) findViewById(R.id.timezone);
                    ImageView weatherImg=(ImageView) findViewById(R.id.weather_image);

                    tempText.setText(String.format("%.0f",temp)+celcius);
                    tempMinmax.setText(String.format("%.0f",minTemp)+celcius+"/"+String.format("%.0f",maxTemp)+celcius);
                    weatherMain.setText(main);
                    weatherDescription.setText(description);
                    cityText.setText(cityName);
                    countryText.setText(countryName);
                    dateText.setText(getMonth(date.getMonth()+1)+" "+date.getDate()+", "+(date.getYear()+1900));
                    timeText.setText(hours+":"+minutes);
                    dayText.setText(getDay(date.getDay()));
                    humidityText.setText(humidity+"%");
                    pressureText.setText(String.format("%.0f",pressure)+"hPa");
                    timezoneText.setText(timezone);
                    Picasso.get().load(iconUrl).into(weatherImg);
                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue  = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private String getMonth(int n)
    {
        switch(n)
        {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
        }
        return "";
    }

    private String getDay(int n)
    {
        switch(n)
        {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            case 7:
                return "Sunday";
        }
        return "";
    }
}