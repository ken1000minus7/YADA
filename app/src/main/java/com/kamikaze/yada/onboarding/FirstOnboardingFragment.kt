package com.kamikaze.yada.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.FragmentAboutdiary1Binding

class FirstOnboardingFragment : Fragment(R.layout.fragment_aboutdiary1) {
    private var _binding: FragmentAboutdiary1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutdiary1Binding.inflate(inflater, container, false)
        val view = binding.root
        val next = binding.next1
        next.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_aboutdiary1_to_aboutdiary2)
        }
        return view
    }
}