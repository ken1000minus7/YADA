package com.kamikaze.yada;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;
import com.kamikaze.yada.diary.Diary;
import com.kamikaze.yada.diary.DiaryHandler;
import com.kamikaze.yada.model.User;

import java.util.ArrayList;

public class MainPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


    }


}