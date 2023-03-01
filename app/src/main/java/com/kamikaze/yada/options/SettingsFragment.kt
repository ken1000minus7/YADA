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

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        val navRail: NavigationRailView = requireActivity().findViewById(R.id.navigation_rail)
        if (navRail.selectedItemId == R.id.customize) {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.option_container, CustomizeFragment::class.java, null).commit()
        }
        val deleteAccount: Button = view.findViewById<View>(R.id.delete_account_button) as Button
        val aboutUs: Button = view.findViewById<View>(R.id.aboutus_button) as Button
        val feedbackButton: Button = view.findViewById<View>(R.id.feedback_button) as Button
        deleteAccount.setOnClickListener {
            val dialog: View = inflater.inflate(R.layout.delete_confirm_dialog, container, false)
            val alertDialog: AlertDialog = AlertDialog.Builder(context).setView(dialog).setPositiveButton("Yes") { dialogInterface, i ->
                dialogInterface.cancel()
                val dialogConfirm: View = inflater.inflate(R.layout.delete_account_dialog, container, false)
                val pass: EditText = dialogConfirm.findViewById<View>(R.id.delete_pass_input) as EditText
                val deleteDialog: AlertDialog = AlertDialog.Builder(context).setView(dialogConfirm).setPositiveButton("Confirm", null).setNegativeButton("Cancel") { dialogInterface, i ->
                    dialogInterface.cancel()
                }.create()
                deleteDialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
                deleteDialog.setOnShowListener { dialogInterface ->
                    val posButton: Button = deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE) as Button
                    posButton.setOnClickListener {
                        val password: String = pass.text.toString()
                        val credential: AuthCredential = EmailAuthProvider.getCredential((FirebaseAuth.getInstance().currentUser!!.email)!!, password)
                        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                dialogInterface.cancel()
                                val progressDialog: ProgressDialog = ProgressDialog(context)
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                                progressDialog.setCanceledOnTouchOutside(false)
                                progressDialog.setMessage("Deleting account")
                                progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
                                progressDialog.show()
                                val uid: String? = FirebaseAuth.getInstance().uid
                                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                                val intent: Intent = Intent(activity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                                val documentReference: DocumentReference = db.collection("users").document((uid)!!)
                                documentReference.delete().addOnCompleteListener { task1: Task<Void?> ->
                                    if (task1.isSuccessful) {
                                        FirebaseAuth.getInstance().signOut()
                                        user!!.delete().addOnCompleteListener { task1 ->
                                            progressDialog.cancel()
                                            if (task1.isSuccessful) {
                                                Toast.makeText(context, "Account deleted", Toast.LENGTH_SHORT).show()
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                startActivity(intent)
                                            }
                                        }
                                    } else {
                                        progressDialog.cancel()
                                    }
                                }
                            } else Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                deleteDialog.show()
            }.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int -> dialogInterface.cancel() }).create()
            alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
            alertDialog.show()
        }
        aboutUs.setOnClickListener(View.OnClickListener {
            Toast.makeText(context, "We are good people", Toast.LENGTH_SHORT).show()
            if (activity != null) {
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.option_container, TeamKamikazeFragment::class.java, null)
                        .setReorderingAllowed(true).commit()
            }
        })
        feedbackButton.setOnClickListener {
            val dialog: View = inflater.inflate(R.layout.feedback_dialog, container, false)
            val alertDialog: AlertDialog = AlertDialog.Builder(context).setView(dialog).setPositiveButton("Send feedback", null).setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                public override fun onClick(dialogInterface: DialogInterface, i: Int) {
                    dialogInterface.cancel()
                }
            }).create()
            alertDialog.setOnShowListener { dialogInterface ->
                val posButton: Button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE) as Button
                posButton.setOnClickListener {
                    val feedbackText: EditText = dialog.findViewById<View>(R.id.feedback_input) as EditText
                    if ((feedbackText.text.toString() == "")) {
                        Toast.makeText(context, "Please enter some feedback", Toast.LENGTH_SHORT).show()
                    } else {
                        dialogInterface.cancel()
                        val progressDialog: ProgressDialog = ProgressDialog(context)
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                        progressDialog.setMessage("Sending feedback")
                        progressDialog.setCanceledOnTouchOutside(false)
                        progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
                        progressDialog.show()
                        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                        val feedback: Feedback = Feedback(feedbackText.text.toString(), user!!.email, user.uid)
                        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
                        db.collection("feedbacks").add(feedback).addOnCompleteListener(OnCompleteListener { task: Task<DocumentReference?> ->
                            progressDialog.cancel()
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                            } else Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        })
                    }
                }
            }
            alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
            alertDialog.show()
        }
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