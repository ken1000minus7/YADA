package com.kamikaze.yada.options;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.kamikaze.yada.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecurityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecurityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SecurityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecurityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecurityFragment newInstance(String param1, String param2) {
        SecurityFragment fragment = new SecurityFragment();
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
        View view=inflater.inflate(R.layout.fragment_security, container, false);
        Button emailChange=(Button) view.findViewById(R.id.change_email);
        Button passChange=(Button) view.findViewById(R.id.change_password);
        passChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialog=inflater.inflate(R.layout.password_change_dialog,container,false);
                EditText oldPass=(EditText) dialog.findViewById(R.id.old_input);
                EditText newPass=(EditText) dialog.findViewById(R.id.new_input);
                EditText confirmPass=(EditText) dialog.findViewById(R.id.confirm_input); 
                AlertDialog alertDialog=new AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("change password",null).setNegativeButton("forgot password",null).create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.primary1);
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positive= alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negative=alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(newPass.getText()==null || newPass.getText().toString().equals("") || oldPass.getText()==null || oldPass.getText().toString().equals("") || confirmPass.getText()==null || confirmPass.getText().toString().equals(""))
                                {
                                    Toast.makeText(getContext(), "No field should be left empty", Toast.LENGTH_SHORT).show();
                                    oldPass.setText("");
                                    newPass.setText("");
                                    confirmPass.setText("");
                                    return;
                                }
                                if(!newPass.getText().toString().equals(confirmPass.getText().toString()))
                                {
                                    Toast.makeText(getContext(), "New and confirmed password do not match", Toast.LENGTH_SHORT).show();
                                    oldPass.setText("");
                                    newPass.setText("");
                                    confirmPass.setText("");
                                }
                                else
                                {
                                    String newPassword=newPass.getText().toString();
                                    AuthCredential credential= EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),oldPass.getText().toString());
                                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialogInterface.cancel();
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(getContext(), "Passsword updated successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                                oldPass.setText("");
                                                newPass.setText("");
                                                confirmPass.setText("");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dialogInterface.cancel();
                                        if(task.isSuccessful())
                                        {

                                            Toast.makeText(getContext(), "Password reset e-mail sent to your registered e-mail account", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });
        emailChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialog=inflater.inflate(R.layout.email_change_dialog,container,false);
                EditText passInput=(EditText) dialog.findViewById(R.id.pass_input);
                EditText emailInput=(EditText) dialog.findViewById(R.id.email_input); 
                AlertDialog alertDialog=new AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("change e-mail",null).create();
                alertDialog.getWindow().setBackgroundDrawableResource(R.color.primary1);
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positive=alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String password = passInput.getText().toString();
                                String newEmail = emailInput.getText().toString();
                                if (password == null || password.equals("") || newEmail == null || newEmail.equals("")) {
                                    Toast.makeText(getContext(), "No field can be empty", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {

                                    AuthCredential credential=EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(),password);
                                    FirebaseAuth.getInstance().getCurrentUser().reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                FirebaseAuth.getInstance().getCurrentUser().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        dialogInterface.cancel();
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(getContext(), "E-mail updated successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                        {
                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                       
                    }
                });
                alertDialog.show();
            }
        });
        return view;
    }
}