package com.kamikaze.yada.options;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        TextView emailText=(TextView) view.findViewById(R.id.email_text);
        emailText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ImageButton profileEdit=(ImageButton) view.findViewById(R.id.profile_pic_edit);
        ImageView profilePic=(ImageView) view.findViewById(R.id.profile_pic);
        String pfpUrl=((OptionsActivity) getActivity()).pfpUrl;
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        DocumentReference document=db.collection("users").document(FirebaseAuth.getInstance().getUid());
        TextView nameText=(TextView) view.findViewById(R.id.name);
        ImageView aboutEditButton=(ImageView) view.findViewById(R.id.about_edit_img);
        ImageView aboutDoneButton=(ImageView) view.findViewById(R.id.about_done_img);
        EditText aboutEdit=(EditText) view.findViewById(R.id.about_edit);
        TextView aboutText=(TextView) view.findViewById(R.id.about_text);
        aboutEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(aboutText.getVisibility()==View.VISIBLE)
//                {
                    String oldAbout=aboutText.getText().toString();
                    aboutEdit.setText(oldAbout);
//                    aboutEditButton.setImageResource(R.color.primary1);
//                    aboutEditButton.setImageResource(R.drawable.ic_done);
                    aboutEdit.setVisibility(View.VISIBLE);
                    aboutText.setVisibility(View.INVISIBLE);
                    aboutEditButton.setVisibility(View.INVISIBLE);
                    aboutDoneButton.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//
//                }
            }
        });

        aboutDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAbout=aboutEdit.getText().toString();
                aboutText.setText(newAbout);
                aboutText.setVisibility(View.VISIBLE);
                aboutEdit.setVisibility(View.INVISIBLE);
                aboutDoneButton.setVisibility(View.INVISIBLE);
                aboutEditButton.setVisibility(View.VISIBLE);
                document.update("about",newAbout).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(), "About updated", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });



        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    String imageUrl= (String) task.getResult().get("imageUrl");
                    String name= (String) task.getResult().get("displayName");
                    String about=(String) task.getResult().get("about");
                    aboutText.setText(about);
                    nameText.setText(name);
                    if(imageUrl!=null && !imageUrl.equals("") && !imageUrl.equals("null"))
                    {
                        if(profilePic!=null)Picasso.get().load(imageUrl).into(profilePic);
                        else
                            Toast.makeText(getContext(), "Sadge not working", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        String[] options={"Upload from gallery","Take a new picture"};
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(getContext()).setTitle("Change profile photo").setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i)
                        {
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                if(intent.resolveActivity(getActivity().getPackageManager())!=null)
                                {
                                    getActivity().startActivityForResult(intent,1);
                                }
                                break;

                            case 1:
                                Intent intent1= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File file=new File(Environment.getExternalStorageDirectory(),"pfp.png");
                                intent1.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), getActivity().getApplicationContext().getPackageName() + ".provider", file));
                                intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                if(intent1.resolveActivity(getActivity().getPackageManager())!=null)
                                {
                                    getActivity().startActivityForResult(intent1,2);
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