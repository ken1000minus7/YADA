package com.kamikaze.yada

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    var mMap: GoogleMap? = null
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener!!)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        view.setOnTouchListener { view, motionEvent ->
            view.parent.parent.parent.requestDisallowInterceptTouchEvent(true)
            false
        }
        mapFragment?.getMapAsync(OnMapReadyCallback { googleMap ->
            val viewPager = requireActivity().findViewById<View>(R.id.main_fragment_container) as ViewPager2
            if (viewPager != null) {
                viewPager.isUserInputEnabled = false
                mMap = googleMap
                val nightModeFlags = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                    googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_night))
                }
                locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    mMap!!.clear()
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap!!.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val listAddress = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (listAddress != null && listAddress.size > 0) {
                            var address: String? = ""
                            if (listAddress[0].adminArea != null) {
                                address += listAddress[0].adminArea + " " // state name
                            }
                            if (listAddress[0].locality != null) {
                                address += listAddress[0].locality + " " // city name
                            }
                            if (listAddress[0].thoroughfare != null) {
                                address += listAddress[0].thoroughfare + " " // street name
                            }
                            if (listAddress[0].postalCode != null) {
                                address += listAddress[0].postalCode // Postal Code
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (locationManager != null) {
                if (Build.VERSION.SDK_INT < 23) {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return@OnMapReadyCallback
                    }
                    locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener as LocationListener)
                } else {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
                    } else {
                        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener as LocationListener)
                        val lastKnownLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (lastKnownLocation != null) {
                            mMap!!.clear()
                            val userLocation = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                            mMap!!.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
                        }
                    }
                }
            }
        })
    }
}