package com.kamikaze.yada.diary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kamikaze.yada.R;

public class MainFragment extends Fragment {



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {

    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        floatingActionButton=(FloatingActionButton) view.findViewById(R.id.floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewed) {
                View dialogView=inflater.inflate(R.layout.diary_input_dialog,container,false);
                EditText titleView=(EditText) dialogView.findViewById(R.id.title_input);
                EditText descriptionView=(EditText) dialogView.findViewById(R.id.description_input);
                EditText location=(EditText) dialogView.findViewById(R.id.location_input);
                new AlertDialog.Builder(view.getContext()).setView(dialogView).setTitle("Create new diary").setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DiaryHandler diaryHandler=new DiaryHandler(getContext());
                        Diary diary=new Diary(titleView.getText().toString(),descriptionView.getText().toString(), location.getText().toString());
//                        diaryHandler.loadData(view.findViewById(R.id.list));
                        diaryHandler.addDiary(diary,view.findViewById(R.id.list));
                        dialogInterface.cancel();
                    }
                }).show();
            }
        });
        return view;
    }
}