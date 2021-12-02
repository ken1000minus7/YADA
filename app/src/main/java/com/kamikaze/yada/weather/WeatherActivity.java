package com.kamikaze.yada.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class WeatherActivity extends AppCompatActivity {


    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appid = "cd0755a868710f3da85d3e63a838080e";
    DecimalFormat df = new DecimalFormat("#.##");
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
//        etCity = findViewById(R.id.etCity);
//        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        Button weatherButton=(Button) findViewById(R.id.btnGet);
//        weatherButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });
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
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.QUALITY_BALANCED_POWER_ACCURACY, null).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful())
                {
                    Location userLocation= task.getResult();
                    if(userLocation==null)
                    {
                        Log.d("what","location null ig");
                        Toast.makeText(getApplicationContext(), "Couldn't get your location", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double latitude=userLocation.getLatitude();
                    double longitude= userLocation.getLongitude();
                    Log.d("location",latitude+" "+longitude);
//                    String tempUrl = "";
//                    String City = etCity.getText().toString().trim();
//                    String Country = etCountry.getText().toString().trim();
//                    if (City.equals("")){
//                        tvResult.setText("City field cannot be empty");
//                    }else{
//                        if (!Country.equals("")){
//                            tempUrl = url + "?q=" + City+ ","+ Country+"&appid="+ appid;
//                        }else {
//                            tempUrl = url + "?q=" + City+"&appid="+ appid;
//                        }

                    String tempUrl=url+"?lat="+latitude+"&lon="+longitude+"&appid="+appid;
                    Log.d("url",tempUrl);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Log.d("response", response);
                                String output = "";
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                    String description = jsonObjectWeather.getString("description");
                                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                                    double temp = jsonObjectMain.getDouble("temp") - 273.15 ;
                                    //          double feelsLike = jsonObjectMain.getDouble("feels Like ") -273.15;
                                    float pressure = jsonObjectMain.getInt("pressure");
                                    int humidity = jsonObjectMain.getInt("humidity");
                                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                                    String wind = jsonObjectWind.getString("speed");
                                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                                    String clouds = jsonObjectClouds.getString("all");
                                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                                    // String countryName = jsonObjectSys.getString("Country");
                                    String cityName = jsonResponse.getString("name");
                                    tvResult.setTextColor(Color.rgb(68,134,199));
                                    output += "Current weather of "+cityName//+"("+countryName+")"
                                            +"\n Temp:"+df.format(temp)+"C"
                                            //       +"\n Feels Like:"+df.format(feelsLike)+"C"
                                            +"\n Humidity:"+humidity+"%"
                                            +"\n Description:"+description
                                            +"\n Wind Speed:"+wind+"m/s"
                                            +"\n Cloudiness:"+clouds+"%"
                                            +"\n Pressure:"+pressure+"hPa";
                                    tvResult.setText(output);
                                }catch (JSONException e){
                                    e.printStackTrace();

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue requestQueue  = Volley.newRequestQueue(getApplicationContext());
                        requestQueue.add(stringRequest);

                }
                else Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}