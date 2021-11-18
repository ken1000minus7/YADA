package com.kamikaze.yada.options;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kamikaze.yada.MainActivity;
import com.kamikaze.yada.MainPageActivity;
import com.kamikaze.yada.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class OptionsActivity extends AppCompatActivity {
    String pfpUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        NavigationRailView navRail=(NavigationRailView) findViewById(R.id.navigation_rail);
        ConstraintLayout rootLayout=(ConstraintLayout) findViewById(R.id.option_layout);
        Intent intent=getIntent();
        int position=intent.getIntExtra("position",0);
        pfpUrl=intent.getStringExtra("pfpUrl");

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
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView aboutText=(TextView) findViewById(R.id.about_text);
        TextView nameText=(TextView) findViewById(R.id.name);
        ImageView profilePic=(ImageView) findViewById(R.id.profile_pic);

        Log.d("datahoho", String.valueOf(requestCode));
        if(resultCode!=RESULT_OK || data==null)
        {
            if(data==null) Log.d("stopped","data null");
            else Log.d("stopped","result cancel");
            if(data==null)Toast.makeText(this, "Didn't work, try something else maybe", Toast.LENGTH_SHORT).show();
            Picasso.get().load("https://i.kym-cdn.com/photos/images/newsfeed/000/754/538/454.jpg").into(profilePic);
            nameText.setText("Dio Brando");
            aboutText.setText("You thought it was your profile pic, BUT IT WAS ME! DIO!");
            return;
        }

        Log.d("lol","didnt stop");
        switch(requestCode)
        {
            case 1:
                Uri uri= (Uri) data.getData();
                try {
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    profilePic.setImageBitmap(bitmap);
                    updateProfilePic(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("gallery",data.getDataString());
                break;

            case 2:
                uri= (Uri) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
                Log.d("camera",uri.toString());
                break;
        }
    }

    private void updateProfilePic(Uri uri)
    {
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storage=firebaseStorage.getReference(FirebaseAuth.getInstance().getUid()+"/pfp.jpg");
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        DocumentReference document=db.collection("users").document(FirebaseAuth.getInstance().getUid());
        storage.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                pfpUrl=task.getResult().toString();
                                document.update("imageUrl",task.getResult().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(), "Profile pic updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("pfpUrl",pfpUrl);
        setResult(RESULT_OK,intent);
        finish();
    }
}