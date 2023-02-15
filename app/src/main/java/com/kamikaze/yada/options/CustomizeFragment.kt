package com.kamikaze.yada.options

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.kamikaze.yada.R

class CustomizeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_customize, container, false)
        val radioGroup = view.findViewById<View>(R.id.theme_radio_group) as RadioGroup
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            radioGroup.check(R.id.light_button)
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            radioGroup.check(R.id.dark_button)
        } else {
            radioGroup.check(R.id.default_button)
        }
        radioGroup.setOnCheckedChangeListener { radioGroup1: RadioGroup?, i: Int ->
            val preferences = requireActivity().getSharedPreferences("Pref", Context.MODE_PRIVATE)
            val editor = preferences.edit()
            when (i) {
                R.id.default_button -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    editor.putInt("theme", 0)
                }
                R.id.light_button -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putInt("theme", 1)
                }
                R.id.dark_button -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putInt("theme", 2)
                }
            }
            editor.apply()
        }
        return view
    }
}