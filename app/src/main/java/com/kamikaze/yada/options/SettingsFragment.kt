package com.kamikaze.yada.options

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigationrail.NavigationRailView
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.Feedback
import com.kamikaze.yada.MainActivity
import com.kamikaze.yada.R
import com.kamikaze.yada.TeamKamikazeFragment

class SettingsFragment constructor() : Fragment() {
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        val navRail: NavigationRailView = requireActivity().findViewById(R.id.navigation_rail)
        if (navRail.getSelectedItemId() == R.id.customize) {
            val fragmentManager: FragmentManager = requireActivity().getSupportFragmentManager()
            fragmentManager.beginTransaction().replace(R.id.option_container, CustomizeFragment::class.java, null).commit()
        }
        val deleteAccount: Button = view.findViewById<View>(R.id.delete_account_button) as Button
        val aboutUs: Button = view.findViewById<View>(R.id.aboutus_button) as Button
        val feedbackButton: Button = view.findViewById<View>(R.id.feedback_button) as Button
        deleteAccount.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val dialog: View = inflater.inflate(R.layout.delete_confirm_dialog, container, false)
                val alertDialog: AlertDialog = AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        dialogInterface.cancel()
                        val dialogConfirm: View = inflater.inflate(R.layout.delete_account_dialog, container, false)
                        val pass: EditText = dialogConfirm.findViewById<View>(R.id.delete_pass_input) as EditText
                        val deleteDialog: AlertDialog = AlertDialog.Builder(getContext()).setView(dialogConfirm).setPositiveButton("Confirm", null).setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                            public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                                dialogInterface.cancel()
                            }
                        }).create()
                        deleteDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
                        deleteDialog.setOnShowListener(object : OnShowListener {
                            public override fun onShow(dialogInterface: DialogInterface) {
                                val posButton: Button = deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE) as Button
                                posButton.setOnClickListener(object : View.OnClickListener {
                                    public override fun onClick(view: View) {
                                        val password: String = pass.getText().toString()
                                        val credential: AuthCredential = EmailAuthProvider.getCredential((FirebaseAuth.getInstance().getCurrentUser()!!.getEmail())!!, password)
                                        FirebaseAuth.getInstance().getCurrentUser()!!.reauthenticate(credential).addOnCompleteListener(object : OnCompleteListener<Void?> {
                                            public override fun onComplete(task: Task<Void?>) {
                                                if (task.isSuccessful()) {
                                                    dialogInterface.cancel()
                                                    val progressDialog: ProgressDialog = ProgressDialog(getContext())
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                                                    progressDialog.setCanceledOnTouchOutside(false)
                                                    progressDialog.setMessage("Deleting account")
                                                    progressDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
                                                    progressDialog.show()
                                                    val uid: String? = FirebaseAuth.getInstance().getUid()
                                                    val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
                                                    val intent: Intent = Intent(getActivity(), MainActivity::class.java)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                                                    val documentReference: DocumentReference = db.collection("users").document((uid)!!)
                                                    documentReference.delete().addOnCompleteListener(OnCompleteListener({ task1: Task<Void?> ->
                                                        if (task1.isSuccessful()) {
                                                            FirebaseAuth.getInstance().signOut()
                                                            user!!.delete().addOnCompleteListener(object : OnCompleteListener<Void?> {
                                                                public override fun onComplete(task1: Task<Void?>) {
                                                                    progressDialog.cancel()
                                                                    if (task1.isSuccessful()) {
                                                                        Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show()
                                                                        startActivity(intent)
                                                                    } else {
                                                                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                                                                        startActivity(intent)
                                                                    }
                                                                }
                                                            })
                                                        } else {
                                                            progressDialog.cancel()
                                                        }
                                                    }))
                                                } else Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }
                                })
                            }
                        })
                        deleteDialog.show()
                    }
                }).setNegativeButton("No", DialogInterface.OnClickListener({ dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() })).create()
                alertDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
                alertDialog.show()
            }
        })
        aboutUs.setOnClickListener(View.OnClickListener({ view1: View? ->
            Toast.makeText(getContext(), "We are good people", Toast.LENGTH_SHORT).show()
            if (getActivity() != null) {
                val fragmentManager: FragmentManager = requireActivity().getSupportFragmentManager()
                fragmentManager.beginTransaction().replace(R.id.option_container, TeamKamikazeFragment::class.java, null)
                        .setReorderingAllowed(true).commit()
            }
        }))
        feedbackButton.setOnClickListener(View.OnClickListener({ view12: View? ->
            val dialog: View = inflater.inflate(R.layout.feedback_dialog, container, false)
            val alertDialog: AlertDialog = AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("Send feedback", null).setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    dialogInterface.cancel()
                }
            }).create()
            alertDialog.setOnShowListener(object : OnShowListener {
                public override fun onShow(dialogInterface: DialogInterface) {
                    val posButton: Button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) as Button
                    posButton.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(view12: View) {
                            val feedbackText: EditText = dialog.findViewById<View>(R.id.feedback_input) as EditText
                            if ((feedbackText.getText().toString() == "")) {
                                Toast.makeText(getContext(), "Please enter some feedback", Toast.LENGTH_SHORT).show()
                            } else {
                                dialogInterface.cancel()
                                val progressDialog: ProgressDialog = ProgressDialog(getContext())
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                                progressDialog.setMessage("Sending feedback")
                                progressDialog.setCanceledOnTouchOutside(false)
                                progressDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
                                progressDialog.show()
                                val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser()
                                val feedback: Feedback = Feedback(feedbackText.getText().toString(), user!!.getEmail(), user.getUid())
                                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                                db.collection("feedbacks").add(feedback).addOnCompleteListener(OnCompleteListener({ task: Task<DocumentReference?> ->
                                    progressDialog.cancel()
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                                    } else Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                                }))
                            }
                        }
                    })
                }
            })
            alertDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
            alertDialog.show()
        }))
        return view
    }

    public override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val navRail: NavigationRailView = requireActivity().findViewById(R.id.navigation_rail)
        if (navRail.getSelectedItemId() == R.id.customize) {
            val fragmentManager: FragmentManager = requireActivity().getSupportFragmentManager()
            fragmentManager.beginTransaction().replace(R.id.option_container, CustomizeFragment::class.java, null).commit()
        }
    }
}