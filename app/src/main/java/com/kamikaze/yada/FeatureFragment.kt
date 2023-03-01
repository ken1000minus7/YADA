package com.kamikaze.yada;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.kamikaze.yada.pathtracker.PathTracker;

public class FeatureFragment extends Fragment {


    public FeatureFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_feature, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        ImageButton hikerButton=(ImageButton) view.findViewById(R.id.hiker_button);
        hikerButton.setOnClickListener(view13 -> {
            Intent intent=new Intent(getContext(),HikersWatchActivity.class);
            startActivity(intent);
        });
        ImageButton pathButton = (ImageButton) view.findViewById(R.id.path_button);
        pathButton.setOnClickListener(view1 -> {
            Intent intent=new Intent(getContext(), PathTracker.class);
            startActivity(intent);
        });
        ImageButton memorableButton=(ImageButton) view.findViewById(R.id.memorable_button);
        memorableButton.setOnClickListener(view12 -> {
            Intent intent=new Intent(getActivity(),MemorablePlacesActivity.class);
            startActivity(intent);
        });
        return view;
    }
}