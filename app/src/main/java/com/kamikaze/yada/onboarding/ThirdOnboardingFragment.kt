package com.kamikaze.yada.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.FragmentAboutdiary3Binding


class ThirdOnboardingFragment : Fragment(R.layout.fragment_aboutdiary3) {
    private var _binding: FragmentAboutdiary3Binding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutdiary3Binding.inflate(inflater, container, false)
        val view = binding.root
        val next = binding.next3
        next.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_aboutdiary3_to_landingFragment)
        }
        return view
    }
}