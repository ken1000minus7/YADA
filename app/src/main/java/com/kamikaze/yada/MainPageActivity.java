package com.kamikaze.yada;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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
        DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView sidebar=(NavigationView) findViewById(R.id.sidebar);
        sidebar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.logout)
                {
                    View confirmDialog= LayoutInflater.from(MainPageActivity.this).inflate(R.layout.confirm_dialog,drawerLayout,false);
                    new AlertDialog.Builder(MainPageActivity.this).setView(confirmDialog).setTitle("Are you sure you want to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(MainPageActivity.this,MainActivity.class);
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(MainPageActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }
                return false;
            }
        });
    }


}