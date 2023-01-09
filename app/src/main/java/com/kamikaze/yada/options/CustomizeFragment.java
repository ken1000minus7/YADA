package com.kamikaze.yada.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.kamikaze.yada.R;

public class CustomizeFragment extends Fragment {

    public CustomizeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customize, container, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.theme_radio_group);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            radioGroup.check(R.id.light_button);
        } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            radioGroup.check(R.id.dark_button);
        } else {
            radioGroup.check(R.id.default_button);
        }
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            SharedPreferences preferences = getActivity().getSharedPreferences("Pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            switch (i) {
                case R.id.default_button:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    editor.putInt("theme", 0);
                    break;
                case R.id.light_button:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putInt("theme", 1);
                    break;
                case R.id.dark_button:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putInt("theme", 2);
                    break;
            }
            editor.apply();
        });
        return view;
    }
}