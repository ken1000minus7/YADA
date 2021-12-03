package com.kamikaze.yada.pathtracker

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.FragmentTrackingBinding
import com.kamikaze.yada.pathtracker.Constants.ACTION_PAUSE_SERVICE
import com.kamikaze.yada.pathtracker.Constants.ACTION_START_OR_RESUME_SERVICE
import com.kamikaze.yada.pathtracker.Constants.ACTION_STOP_SERVICE
import com.kamikaze.yada.pathtracker.Constants.MAP_ZOOM
import com.kamikaze.yada.pathtracker.Constants.POLYLINE_COLOR
import com.kamikaze.yada.pathtracker.Constants.POLYLINE_WIDTH


class TrackingFragment : Fragment(R.layout.fragment_tracking) {
    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    lateinit var map : GoogleMap
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    lateinit var mapView:MapView
    lateinit var btnToggleRun : MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnToggleRun = view.findViewById<MaterialButton>(R.id.btnToggleRun)
        val btnFinishRun = binding.btnFinishRun
        val act = activity as PathTracker
        btnToggleRun.setOnClickListener {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            toggleRun()
        }
        btnFinishRun.setOnClickListener {
            showCancelTrackingDialog(view)
        }
        mapView = view.findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync{
            map = it
            addAllPolylines()
        }
        subscribeToObservers()
    }
    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size>1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size -2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }
    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })
    }
    private fun toggleRun(){
        if(isTracking) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }
    private fun sendCommandToService(action: String) =
        Intent(activity as PathTracker, TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }
    private fun updateTracking(isTracking: Boolean) {
        val btnFinishRun = binding.btnFinishRun

        this.isTracking = isTracking
        if(!isTracking) {
            btnToggleRun.text = "Start"
            btnFinishRun.visibility = View.VISIBLE
        } else {
            btnToggleRun.text = "Stop"
            btnFinishRun.visibility = View.GONE
        }
    }
//    fun calculatePolylineLength(polyline: Polyline):Float{
//        var dis = 0F
//        for (i in 0..polyline.size -2){
//            val pos1 = polyline[i]
//            val pos2 = polyline[i+1]
//            val result = FloatArray(1)
//            Location.distanceBetween(
//                pos1.latitude,
//                pos1.longitude,pos2.latitude,pos2.longitude, result
//            )
//            dis+=result[0]
//        }
//        return dis
//    }

    private fun showCancelTrackingDialog(view: View) {
        val act = activity as PathTracker
        val dialog = MaterialAlertDialogBuilder(act, R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes") { _, _ ->
                stopRun()
                Navigation.findNavController(view).navigate(R.id.action_trackingFragment_to_featureFragment)

            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }
    private fun stopRun() {
        sendCommandToService(ACTION_STOP_SERVICE)
//       val intent = Intent(activity , FeatureFragment::class.java )
//        activity?.startActivity(intent)
//        activity?.finish()

    }


    private fun randomString(i: Int): String {
        val characters = "abcdefghijklmnopqrstuvwxyz"
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(i) { charset.random() }
            .joinToString("")
    }

    private fun addAllPolylines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)

        }
    }

    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }


    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

}