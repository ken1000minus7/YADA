package com.kamikaze.yada.diary;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kamikaze.yada.R;
import com.kamikaze.yada.TranslateActivity;
import com.kamikaze.yada.diary.writenotes.WriteActivity;
import com.kamikaze.yada.weather.WeatherActivity;
import com.kamikaze.yada.webview.BlogActivity;

import java.util.ArrayList;

public class MainFragment extends Fragment {


    public static ArrayList<Diary> originalList=null;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean fabOpen=false;
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
        FloatingActionButton diaryButton=(FloatingActionButton) view.findViewById(R.id.floating_diary_button);
        FloatingActionButton translateButton=(FloatingActionButton) view.findViewById(R.id.floating_translate_button);
        FloatingActionButton newsButton=(FloatingActionButton) view.findViewById(R.id.floating_news_button);
        FloatingActionButton weatherButton=(FloatingActionButton) view.findViewById(R.id.floating_weather_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewed) {
                if(fabOpen)
                {
                    fabOpen=false;
                    diaryButton.animate().translationY(0);
                    translateButton.animate().translationY(0);
                    newsButton.animate().translationY(0);
                    weatherButton.animate().translationY(0);
                    floatingActionButton.setImageResource(R.drawable.ic_plus_button);
                    floatingActionButton.setBackgroundColor(Color.WHITE);
                }
                else
                {
                    fabOpen=true;
                    diaryButton.animate().translationY(-getResources().getDimension(R.dimen._168sdp));
                    translateButton.animate().translationY(-getResources().getDimension(R.dimen._126sdp));
                    newsButton.animate().translationY(-getResources().getDimension(R.dimen._84sdp));
                    weatherButton.animate().translationY(-getResources().getDimension(R.dimen._44sdp));
                    floatingActionButton.setImageResource(R.drawable.ic_close);
                    floatingActionButton.setBackgroundColor(Color.BLACK);
                }
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity().getApplicationContext(), TranslateActivity.class);
                startActivity(intent);
            }
        });
        newsButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent= new Intent(getActivity().getApplicationContext(), BlogActivity.class);
            startActivity(intent);
        }
    }
        );

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity().getApplicationContext(), WeatherActivity.class);
                startActivity(intent);
            }
        });
        diaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewed) {
                View dialogView=inflater.inflate(R.layout.diary_input_dialog,container,false);
                EditText titleView=(EditText) dialogView.findViewById(R.id.title_input);
                EditText descriptionView=(EditText) dialogView.findViewById(R.id.description_input);
                EditText location=(EditText) dialogView.findViewById(R.id.location_input);
                AlertDialog alertDialog=new AlertDialog.Builder(view.getContext()).setView(dialogView).setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DiaryHandler diaryHandler=new DiaryHandler(getContext());
//                        Notes note=new Notes("Trip to New York","Very good trip","New York","I went to new york and saw spidey boi");
                        Diary diary=new Diary(titleView.getText().toString(),descriptionView.getText().toString(), location.getText().toString());
//                        diaryHandler.loadData(view.findViewById(R.id.list));
                        RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.list);
                        int position=recyclerView.getAdapter().getItemCount();
                        diaryHandler.addDiary(diary,recyclerView);
                        diaryHandler.loadData();

                        Intent intent1 = new Intent(getActivity() , WriteActivity.class);
                        intent1.putExtra("position",position);
                        intent1.putExtra("title",titleView.getText().toString());
                        Log.d("intent", titleView.getText().toString());
                        startActivity(intent1);
                        dialogInterface.cancel();

                    }


                }).create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                alertDialog.show();
            }
        });



        TextView startDiary=(TextView) view.findViewById(R.id.start_diary);
        TextView noResult=(TextView) view.findViewById(R.id.empty_result);
        SearchView searchView=(SearchView) view.findViewById(R.id.search_view);
        SearchManager searchManager=(SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        setupStuff(view,searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.list);
                DiaryListRecyclerViewAdapter diaryListRecyclerViewAdapter=(DiaryListRecyclerViewAdapter) recyclerView.getAdapter();
                if(originalList==null)
                {
                    originalList=diaryListRecyclerViewAdapter.itemList;
                }
                ArrayList<Diary> result=new ArrayList<>();
                for(int i=0;i<originalList.size();i++)
                {
                    Diary diary=originalList.get(i);
                    if(diary.getLocation().toLowerCase().contains(s.toLowerCase()) || diary.getTitle().toLowerCase().contains(s.toLowerCase()) || diary.getDescription().toLowerCase().contains(s.toLowerCase()))
                    {
                        result.add(diary);
                    }
                }
                DiaryListRecyclerViewAdapter adapter=new DiaryListRecyclerViewAdapter(getContext(),result,recyclerView);
                recyclerView.swapAdapter(adapter,false);
                startDiary.setVisibility(View.INVISIBLE);
                if(result.size()==0) noResult.setVisibility(View.VISIBLE);
                else noResult.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.list);
                DiaryListRecyclerViewAdapter diaryListRecyclerViewAdapter=(DiaryListRecyclerViewAdapter) recyclerView.getAdapter();
                if(originalList==null)
                {
                    originalList=diaryListRecyclerViewAdapter.itemList;
                }
                ArrayList<Diary> result=new ArrayList<>();
                for(int i=0;i<originalList.size();i++)
                {
                    Diary diary=originalList.get(i);
                    if(diary.getLocation().toLowerCase().contains(s.toLowerCase()) || diary.getTitle().toLowerCase().contains(s.toLowerCase()) || diary.getDescription().toLowerCase().contains(s.toLowerCase()))
                    {
                        result.add(diary);
                    }
                }
                DiaryListRecyclerViewAdapter adapter=new DiaryListRecyclerViewAdapter(getContext(),result,recyclerView);
                recyclerView.swapAdapter(adapter,false);
                startDiary.setVisibility(View.INVISIBLE);
                if(result.size()==0) noResult.setVisibility(View.VISIBLE);
                else noResult.setVisibility(View.INVISIBLE);
                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(originalList!=null)
                {
                    RecyclerView recyclerView=(RecyclerView) view.findViewById(R.id.list);
                    DiaryListRecyclerViewAdapter adapter=new DiaryListRecyclerViewAdapter(getContext(),originalList,recyclerView);
                    recyclerView.swapAdapter(adapter,false);
                    if(originalList.size()==0) startDiary.setVisibility(View.VISIBLE);
                    originalList=null;
                    noResult.setVisibility(View.INVISIBLE);
                }

                return false;
            }
        });

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        TextView noResult=(TextView) getView().findViewById(R.id.empty_result);
        noResult.setVisibility(View.INVISIBLE);
    }
}