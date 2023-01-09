package com.kamikaze.yada.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.FragmentAboutdiary2Binding

class SecondOnboardingFragment : Fragment(R.layout.fragment_aboutdiary2) {
    private var _binding: FragmentAboutdiary2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutdiary2Binding.inflate(inflater, container, false)
        val view = binding.root
        val next = binding.next2
        next.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_aboutdiary2_to_aboutdiary3)
        }
        return view
    }
}