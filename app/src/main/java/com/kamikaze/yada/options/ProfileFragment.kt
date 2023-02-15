package com.kamikaze.yada.options

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.R
import com.squareup.picasso.Picasso

class ProfileFragment constructor() : Fragment() {
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val emailText: TextView = view.findViewById<View>(R.id.email_text) as TextView
        emailText.setText(FirebaseAuth.getInstance().getCurrentUser()!!.getEmail())
        val profileEdit: ImageButton = view.findViewById<View>(R.id.profile_pic_edit) as ImageButton
        val profilePic: ImageView? = view.findViewById<View>(R.id.profile_pic) as ImageView?
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val document: DocumentReference = db.collection("users").document((FirebaseAuth.getInstance().getUid())!!)
        val nameText: TextView = view.findViewById<View>(R.id.name) as TextView
        val aboutEditButton: ImageView = view.findViewById<View>(R.id.about_edit_img) as ImageView
        val aboutDoneButton: ImageView = view.findViewById<View>(R.id.about_done_img) as ImageView
        val aboutEdit: EditText = view.findViewById<View>(R.id.about_edit) as EditText
        val aboutText: TextView = view.findViewById<View>(R.id.about_text) as TextView
        aboutEditButton.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val oldAbout: String = aboutText.getText().toString()
                aboutEdit.setText(oldAbout)
                aboutEdit.setVisibility(View.VISIBLE)
                aboutText.setVisibility(View.INVISIBLE)
                aboutEditButton.setVisibility(View.INVISIBLE)
                aboutDoneButton.setVisibility(View.VISIBLE)
            }
        })
        aboutDoneButton.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val newAbout: String = aboutEdit.getText().toString()
                aboutText.setText(newAbout)
                aboutText.setVisibility(View.VISIBLE)
                aboutEdit.setVisibility(View.INVISIBLE)
                aboutDoneButton.setVisibility(View.INVISIBLE)
                aboutEditButton.setVisibility(View.VISIBLE)
                val inputMethodManager: InputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                document.update("about", newAbout).addOnCompleteListener(object : OnCompleteListener<Void?> {
                    public override fun onComplete(task: Task<Void?>) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "About updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        })
        val nameEditButton: ImageView = view.findViewById<View>(R.id.name_edit_img) as ImageView
        val nameDoneButton: ImageView = view.findViewById<View>(R.id.name_done_img) as ImageView
        val nameEdit: EditText = view.findViewById<View>(R.id.name_edit) as EditText
        nameEditButton.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val oldname: String = nameText.getText().toString()
                nameEdit.setText(oldname)
                nameEdit.setVisibility(View.VISIBLE)
                nameText.setVisibility(View.INVISIBLE)
                nameEditButton.setVisibility(View.INVISIBLE)
                nameDoneButton.setVisibility(View.VISIBLE)
            }
        })
        nameDoneButton.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val newname: String = nameEdit.getText().toString()
                nameText.setText(newname)
                nameText.setVisibility(View.VISIBLE)
                nameEdit.setVisibility(View.INVISIBLE)
                nameDoneButton.setVisibility(View.INVISIBLE)
                nameEditButton.setVisibility(View.VISIBLE)
                val inputMethodManager: InputMethodManager = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
                document.update("displayName", newname).addOnCompleteListener(object : OnCompleteListener<Void?> {
                    public override fun onComplete(task: Task<Void?>) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        })
        document.get().addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot> {
            public override fun onComplete(task: Task<DocumentSnapshot>) {
                if (task.isSuccessful()) {
                    val imageUrl: String? = task.getResult().get("imageUrl") as String?
                    val name: String? = task.getResult().get("displayName") as String?
                    val about: String? = task.getResult().get("about") as String?
                    aboutText.setText(about)
                    nameText.setText(name)
                    nameEdit.setText(name)
                    aboutEdit.setText(name)
                    if ((imageUrl != null) && !(imageUrl == "") && !(imageUrl == "null")) {
                        if (profilePic != null) Picasso.get().load(imageUrl).into(profilePic) else Toast.makeText(getContext(), "Sadge not working", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        val options: Array<String> = arrayOf("Upload from gallery", "Take a new picture")
        profileEdit.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(getContext()).setTitle("Change profile photo").setItems(options, object : DialogInterface.OnClickListener {
                    public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        when (i) {
                            0 -> {
                                val intent: Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                intent.setType("image/*")
                                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                                    requireActivity().startActivityForResult(intent, 1)
                                }
                            }
                            1 -> {
                                val intent1: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                if (intent1.resolveActivity(requireActivity().getPackageManager()) != null) {
//
                                    requireActivity().startActivityForResult(intent1, 2)
                                }
                            }
                        }
                    }
                })
                alertDialog.show()
            }
        })
        return view
    }
}