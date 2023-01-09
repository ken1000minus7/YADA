package com.kamikaze.yada.options;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kamikaze.yada.R;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView emailText = (TextView) view.findViewById(R.id.email_text);
        emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ImageButton profileEdit = (ImageButton) view.findViewById(R.id.profile_pic_edit);
        ImageView profilePic = (ImageView) view.findViewById(R.id.profile_pic);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference document = db.collection("users").document(FirebaseAuth.getInstance().getUid());
        TextView nameText = (TextView) view.findViewById(R.id.name);
        ImageView aboutEditButton = (ImageView) view.findViewById(R.id.about_edit_img);
        ImageView aboutDoneButton = (ImageView) view.findViewById(R.id.about_done_img);
        EditText aboutEdit = (EditText) view.findViewById(R.id.about_edit);
        TextView aboutText = (TextView) view.findViewById(R.id.about_text);
        aboutEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldAbout = aboutText.getText().toString();
                aboutEdit.setText(oldAbout);
                aboutEdit.setVisibility(View.VISIBLE);
                aboutText.setVisibility(View.INVISIBLE);
                aboutEditButton.setVisibility(View.INVISIBLE);
                aboutDoneButton.setVisibility(View.VISIBLE);
            }
        });

        aboutDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAbout = aboutEdit.getText().toString();
                aboutText.setText(newAbout);
                aboutText.setVisibility(View.VISIBLE);
                aboutEdit.setVisibility(View.INVISIBLE);
                aboutDoneButton.setVisibility(View.INVISIBLE);
                aboutEditButton.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                document.update("about", newAbout).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "About updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        ImageView nameEditButton = (ImageView) view.findViewById(R.id.name_edit_img);
        ImageView nameDoneButton = (ImageView) view.findViewById(R.id.name_done_img);
        EditText nameEdit = (EditText) view.findViewById(R.id.name_edit);
        nameEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldname = nameText.getText().toString();
                nameEdit.setText(oldname);
                nameEdit.setVisibility(View.VISIBLE);
                nameText.setVisibility(View.INVISIBLE);
                nameEditButton.setVisibility(View.INVISIBLE);
                nameDoneButton.setVisibility(View.VISIBLE);
            }
        });

        nameDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newname = nameEdit.getText().toString();
                nameText.setText(newname);
                nameText.setVisibility(View.VISIBLE);
                nameEdit.setVisibility(View.INVISIBLE);
                nameDoneButton.setVisibility(View.INVISIBLE);
                nameEditButton.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                document.update("displayName", newname).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String imageUrl = (String) task.getResult().get("imageUrl");
                    String name = (String) task.getResult().get("displayName");
                    String about = (String) task.getResult().get("about");
                    aboutText.setText(about);
                    nameText.setText(name);
                    nameEdit.setText(name);
                    aboutEdit.setText(name);
                    if (imageUrl != null && !imageUrl.equals("") && !imageUrl.equals("null")) {
                        if (profilePic != null) Picasso.get().load(imageUrl).into(profilePic);
                        else
                            Toast.makeText(getContext(), "Sadge not working", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        String[] options = {"Upload from gallery", "Take a new picture"};
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext()).setTitle("Change profile photo").setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    getActivity().startActivityForResult(intent, 1);
                                }
                                break;

                            case 1:
                                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent1.resolveActivity(getActivity().getPackageManager()) != null) {
//
                                    getActivity().startActivityForResult(intent1, 2);
                                }
                                break;
                        }
                    }
                });
                alertDialog.show();
            }
        });

        return view;
    }
}