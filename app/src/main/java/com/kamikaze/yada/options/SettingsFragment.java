package com.kamikaze.yada.options;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigationrail.NavigationRailView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kamikaze.yada.Feedback;
import com.kamikaze.yada.MainActivity;
import com.kamikaze.yada.R;
import com.kamikaze.yada.TeamKamikazeFragment;

public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        NavigationRailView navRail = getActivity().findViewById(R.id.navigation_rail);
        if (navRail.getSelectedItemId() == R.id.customize) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.option_container, CustomizeFragment.class, null).commit();
        }
        Button deleteAccount = (Button) view.findViewById(R.id.delete_account_button);
        Button aboutUs = (Button) view.findViewById(R.id.aboutus_button);
        Button feedbackButton = (Button) view.findViewById(R.id.feedback_button);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialog = inflater.inflate(R.layout.delete_confirm_dialog, container, false);

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        View dialogConfirm = inflater.inflate(R.layout.delete_account_dialog, container, false);
                        EditText pass = (EditText) dialogConfirm.findViewById(R.id.delete_pass_input);
                        AlertDialog deleteDialog = new AlertDialog.Builder(getContext()).setView(dialogConfirm).setPositiveButton("Confirm", null).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create();
                        deleteDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                        deleteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button posButton = (Button) deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                posButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String password = pass.getText().toString();
                                        AuthCredential credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), password);
                                        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialogInterface.cancel();
                                                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setCanceledOnTouchOutside(false);
                                                    progressDialog.setMessage("Deleting account");
                                                    progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
                                                    progressDialog.show();
                                                    String uid = FirebaseAuth.getInstance().getUid();
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    DocumentReference documentReference = db.collection("users").document(uid);
                                                    documentReference.delete().addOnCompleteListener(task1 -> {

                                                        if (task1.isSuccessful()) {
                                                            FirebaseAuth.getInstance().signOut();
                                                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task1) {
                                                                    progressDialog.cancel();
                                                                    if (task1.isSuccessful()) {
                                                                        Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                                                                        startActivity(intent);
                                                                    } else {
                                                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                                        startActivity(intent);

                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            progressDialog.cancel();
                                                        }
                                                    });

                                                } else
                                                    Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        deleteDialog.show();
                    }
                }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel()).create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                alertDialog.show();
            }
        });
        aboutUs.setOnClickListener(view1 -> {
            Toast.makeText(getContext(), "We are good people", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.option_container, TeamKamikazeFragment.class, null)
                        .setReorderingAllowed(true).commit();

            }
        });
        feedbackButton.setOnClickListener(view12 -> {
            View dialog = inflater.inflate(R.layout.feedback_dialog, container, false);
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("Send feedback", null).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            }).create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    Button posButton = (Button) alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    posButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view12) {
                            EditText feedbackText = (EditText) dialog.findViewById(R.id.feedback_input);
                            if (feedbackText.getText().toString().equals("")) {
                                Toast.makeText(getContext(), "Please enter some feedback", Toast.LENGTH_SHORT).show();
                            } else {
                                dialogInterface.cancel();
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setMessage("Sending feedback");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
                                progressDialog.show();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                Feedback feedback = new Feedback(feedbackText.getText().toString(), user.getEmail(), user.getUid());
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("feedbacks").add(feedback).addOnCompleteListener(task -> {
                                    progressDialog.cancel();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                });

                            }
                        }
                    });
                }
            });
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
            alertDialog.show();
        });
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        NavigationRailView navRail = getActivity().findViewById(R.id.navigation_rail);
        if (navRail.getSelectedItemId() == R.id.customize) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.option_container, CustomizeFragment.class, null).commit();
        }
    }
}