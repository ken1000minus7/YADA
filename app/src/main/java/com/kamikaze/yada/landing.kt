package com.kamikaze.yada

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.kamikaze.yada.databinding.FragmentLandingBinding

class LandingFragment : Fragment(R.layout.fragment_landing){

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.google.setOnClickListener {view: View ->
          Navigation.findNavController(view).navigate(R.id.action_landingFragment_to_loginFragment)

        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

