package com.kamikaze.yada

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kamikaze.yada.pathtracker.PathTracker
import com.kamikaze.yada.databinding.FragmentFeatureBinding


class FeatureFragment : Fragment() {

    lateinit var binding: FragmentFeatureBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFeatureBinding.inflate(inflater, container, false)

        binding.hikerButton.setOnClickListener {
            val intent = Intent(context, HikersWatchActivity::class.java)
            startActivity(intent)
        }

        binding.pathButton.setOnClickListener {
            val intent = Intent(context, PathTracker::class.java)
            startActivity(intent)
        }

        binding.memorableButton.setOnClickListener {
            val intent = Intent(activity, MemorablePlacesActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}