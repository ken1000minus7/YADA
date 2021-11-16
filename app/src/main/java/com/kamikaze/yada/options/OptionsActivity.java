package com.kamikaze.yada.options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.firebase.auth.FirebaseAuth;
import com.kamikaze.yada.MainActivity;
import com.kamikaze.yada.MainPageActivity;
import com.kamikaze.yada.R;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        NavigationRailView navRail=(NavigationRailView) findViewById(R.id.navigation_rail);
        ConstraintLayout rootLayout=(ConstraintLayout) findViewById(R.id.option_layout);
        Intent intent=getIntent();
        int position=intent.getIntExtra("position",0);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        MenuItem item= navRail.getMenu().getItem(position);
        switch(position)
        {
            case 1:
                fragmentTransaction.replace(R.id.option_container,SecurityFragment.class,null).commit();
                item.setChecked(true);
                break;

            case 2:
                fragmentTransaction.replace(R.id.option_container,CustomizeFragment.class,null).commit();
                item.setChecked(true);
                break;

            case 3:
                fragmentTransaction.replace(R.id.option_container,SettingsFragment.class,null).commit();
                item.setChecked(true);
                break;
        }
        navRail.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager=getSupportFragmentManager();

                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                switch(item.getItemId())
                {
                    case R.id.profile:
                        fragmentTransaction.replace(R.id.option_container,ProfileFragment.class,null).commit();
                        break;

                    case R.id.security:
                        fragmentTransaction.replace(R.id.option_container,SecurityFragment.class,null).commit();
                        break;

                    case R.id.customize:
                        fragmentTransaction.replace(R.id.option_container,CustomizeFragment.class,null).commit();
                        break;

                    case R.id.settings:
                        fragmentTransaction.replace(R.id.option_container,SettingsFragment.class,null).commit();
                        break;

                    case R.id.logout:
                        View confirmDialog= LayoutInflater.from(OptionsActivity.this).inflate(R.layout.confirm_dialog,rootLayout,false);
                        new AlertDialog.Builder(OptionsActivity.this).setView(confirmDialog).setTitle("Are you sure you want to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(OptionsActivity.this, MainActivity.class);
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(OptionsActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                        return false;

                }
                item.setChecked(true);
                return false;
            }
        });
    }
}