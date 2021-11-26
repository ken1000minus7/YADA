package com.kamikaze.yada.options;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kamikaze.yada.MainActivity;
import com.kamikaze.yada.R;
import com.kamikaze.yada.diary.DiaryHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_settings, container, false);
        Button deleteAccount=(Button) view.findViewById(R.id.delete_account_button);
        Button aboutUs=(Button) view.findViewById(R.id.aboutus_button);
        Button feedbackButton=(Button) view.findViewById(R.id.feedback_button);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialog=inflater.inflate(R.layout.delete_confirm_dialog,container,false);

                AlertDialog alertDialog=new AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        View dialogConfirm=inflater.inflate(R.layout.delete_account_dialog,container,false);
                        EditText pass=(EditText) dialogConfirm.findViewById(R.id.delete_pass_input);
                        AlertDialog deleteDialog=new AlertDialog.Builder(getContext()).setView(dialogConfirm).setPositiveButton("Confirm",null).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).create();
                        deleteDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                        deleteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                Button posButton=(Button) deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                posButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String password=pass.getText().toString();
                                        AuthCredential credential= EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),password);
                                        FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    dialogInterface.cancel();
                                                    ProgressDialog progressDialog= new ProgressDialog(getContext());
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setCanceledOnTouchOutside(false);
                                                    progressDialog.setMessage("Deleting account");
                                                    progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.empty_list_background);
                                                    progressDialog.show();
                                                    String uid= FirebaseAuth.getInstance().getUid();
                                                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                                    Log.d("uid",uid);

                                                    Log.d("uid",uid);
                                                    Intent intent=new Intent(getActivity(), MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);

                                                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                                                    DocumentReference documentReference=db.collection("users").document(uid);
                                                    documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful())
                                                            {
                                                                FirebaseAuth.getInstance().signOut();

                                                                Log.d("success","deleted");
                                                                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        progressDialog.cancel();
                                                                        if(task.isSuccessful())
                                                                        {
                                                                            Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                                                                            startActivity(intent);
                                                                            Log.d("uid",uid);

                                                                        }
                                                                        else
                                                                        {
                                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                                            startActivity(intent);

                                                                        }
                                                                    }
                                                                });
                                                            }
                                                            else
                                                            {
                                                                Log.d("failed","not deleted");
                                                                progressDialog.cancel();
                                                            }
                                                        }
                                                    });

                                                }
                                                else
                                                    Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        deleteDialog.show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                alertDialog.show();
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "We are good people", Toast.LENGTH_SHORT).show();
            }
        });
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}