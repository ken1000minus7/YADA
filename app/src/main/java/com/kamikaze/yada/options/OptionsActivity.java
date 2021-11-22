package com.kamikaze.yada.options;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kamikaze.yada.MainActivity;
import com.kamikaze.yada.MainPageActivity;
import com.kamikaze.yada.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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
                if(navRail.getSelectedItemId()==item.getItemId()) return false;
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
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

    @SuppressLint("LogNotTimber")
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
//            if(data==null)Toast.makeText(this, "Didn't work, try something else maybe", Toast.LENGTH_SHORT).show();
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
                Bitmap bitmap=(Bitmap) data.getExtras().get("data");
                profilePic.setImageBitmap(bitmap);
                updateProfilePic(bitmap);
                break;
        }
    }

    private void updateProfilePic(Uri uri)
    {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Updating profile photo");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();
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
                                        progressDialog.cancel();
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(), "Profile pic updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else progressDialog.cancel();
                        }
                    });
                }
                else progressDialog.cancel();
            }
        });
    }

    private void updateProfilePic(Bitmap bitmap)
    {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Updating profile photo");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
        progressDialog.setProgressNumberFormat(null);
        progressDialog.setProgressPercentFormat(null);
        progressDialog.show();
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storage=firebaseStorage.getReference(FirebaseAuth.getInstance().getUid()+"/pfp.jpg");
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        DocumentReference document=db.collection("users").document(FirebaseAuth.getInstance().getUid());
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] pfp=baos.toByteArray();
        storage.putBytes(pfp).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                        progressDialog.cancel();
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(), "Profile pic updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else progressDialog.cancel();
                        }
                    });
                }
                else progressDialog.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        TextView aboutText=(TextView) findViewById(R.id.about_text);
        EditText aboutEdit=(EditText) findViewById(R.id.about_edit);
        ImageView aboutEditButton=(ImageView) findViewById(R.id.about_edit_img);
        ImageView aboutDoneButton=(ImageView) findViewById(R.id.about_done_img);
        TextView nameText=(TextView) findViewById(R.id.name);
        EditText nameEdit=(EditText) findViewById(R.id.name_edit);
        ImageView nameEditButton=(ImageView) findViewById(R.id.name_edit_img);
        ImageView nameDoneButton=(ImageView) findViewById(R.id.name_done_img);
        if((aboutText!=null && aboutText.getVisibility()==View.INVISIBLE) || (nameText!=null && nameText.getVisibility()==View.INVISIBLE))
        {
            aboutText.setVisibility(View.VISIBLE);
            aboutEdit.setVisibility(View.INVISIBLE);
            aboutDoneButton.setVisibility(View.INVISIBLE);
            aboutEditButton.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
            nameEdit.setVisibility(View.INVISIBLE);
            aboutEdit.setText(aboutText.getText().toString());
            nameEdit.setText(nameText.getText().toString());
            nameDoneButton.setVisibility(View.INVISIBLE);
            nameEditButton.setVisibility(View.VISIBLE);
        }
        else
        {
            Intent intent=new Intent();
            intent.putExtra("pfpUrl",pfpUrl);
            setResult(RESULT_OK,intent);
            finish();
        }

    }


}