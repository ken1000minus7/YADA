package com.kamikaze.yada.options

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.kamikaze.yada.R

class SecurityFragment constructor() : Fragment() {
    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                     savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_security, container, false)
        val emailChange: Button = view.findViewById<View>(R.id.change_email) as Button
        val passChange: Button = view.findViewById<View>(R.id.change_password) as Button
        passChange.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val dialog: View = inflater.inflate(R.layout.password_change_dialog, container, false)
                val oldPass: EditText = dialog.findViewById<View>(R.id.old_input) as EditText
                val newPass: EditText = dialog.findViewById<View>(R.id.new_input) as EditText
                val confirmPass: EditText = dialog.findViewById<View>(R.id.confirm_input) as EditText
                val alertDialog: AlertDialog = AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("change password", null).setNegativeButton("forgot password", null).create()
                alertDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
                alertDialog.setOnShowListener(object : OnShowListener {
                    public override fun onShow(dialogInterface: DialogInterface) {
                        val positive: Button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        val negative: Button = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        positive.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(view: View) {
                                if ((newPass.getText() == null) || (newPass.getText().toString() == "") || (oldPass.getText() == null) || (oldPass.getText().toString() == "") || (confirmPass.getText() == null) || (confirmPass.getText().toString() == "")) {
                                    Toast.makeText(getContext(), "No field should be left empty", Toast.LENGTH_SHORT).show()
                                    oldPass.setText("")
                                    newPass.setText("")
                                    confirmPass.setText("")
                                    return
                                }
                                if (!(newPass.getText().toString() == confirmPass.getText().toString())) {
                                    Toast.makeText(getContext(), "New and confirmed password do not match", Toast.LENGTH_SHORT).show()
                                    oldPass.setText("")
                                    newPass.setText("")
                                    confirmPass.setText("")
                                } else {
                                    val newPassword: String = newPass.getText().toString()
                                    val credential: AuthCredential = EmailAuthProvider.getCredential((FirebaseAuth.getInstance().getCurrentUser()!!.getEmail())!!, oldPass.getText().toString())
                                    FirebaseAuth.getInstance().getCurrentUser()!!.reauthenticate(credential).addOnCompleteListener(object : OnCompleteListener<Void?> {
                                        public override fun onComplete(task: Task<Void?>) {
                                            if (task.isSuccessful()) {
                                                FirebaseAuth.getInstance().getCurrentUser()!!.updatePassword(newPassword).addOnCompleteListener(object : OnCompleteListener<Void?> {
                                                    public override fun onComplete(task: Task<Void?>) {
                                                        dialogInterface.cancel()
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "Passsword updated successfully", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                })
                                            } else {
                                                Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show()
                                                oldPass.setText("")
                                                newPass.setText("")
                                                confirmPass.setText("")
                                            }
                                        }
                                    })
                                }
                            }
                        })
                        negative.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(view: View) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail((FirebaseAuth.getInstance().getCurrentUser()!!.getEmail())!!).addOnCompleteListener(object : OnCompleteListener<Void?> {
                                    public override fun onComplete(task: Task<Void?>) {
                                        dialogInterface.cancel()
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Password reset e-mail sent to your registered e-mail account", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                })
                            }
                        })
                    }
                })
                alertDialog.show()
            }
        })
        emailChange.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                val dialog: View = inflater.inflate(R.layout.email_change_dialog, container, false)
                val passInput: EditText = dialog.findViewById<View>(R.id.pass_input) as EditText
                val emailInput: EditText = dialog.findViewById<View>(R.id.email_input) as EditText
                val alertDialog: AlertDialog = AlertDialog.Builder(getContext()).setView(dialog).setPositiveButton("change e-mail", null).create()
                alertDialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.dialog_background)
                alertDialog.setOnShowListener(object : OnShowListener {
                    public override fun onShow(dialogInterface: DialogInterface) {
                        val positive: Button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        positive.setOnClickListener(object : View.OnClickListener {
                            public override fun onClick(view: View) {
                                val password: String? = passInput.getText().toString()
                                val newEmail: String? = emailInput.getText().toString()
                                if ((password == null) || (password == "") || (newEmail == null) || (newEmail == "")) {
                                    Toast.makeText(getContext(), "No field can be empty", Toast.LENGTH_SHORT).show()
                                } else {
                                    val credential: AuthCredential = EmailAuthProvider.getCredential((FirebaseAuth.getInstance().getCurrentUser()!!.getEmail())!!, password)
                                    FirebaseAuth.getInstance().getCurrentUser()!!.reauthenticate(credential).addOnCompleteListener(object : OnCompleteListener<Void?> {
                                        public override fun onComplete(task: Task<Void?>) {
                                            if (task.isSuccessful()) {
                                                FirebaseAuth.getInstance().getCurrentUser()!!.updateEmail(newEmail).addOnCompleteListener(object : OnCompleteListener<Void?> {
                                                    public override fun onComplete(task: Task<Void?>) {
                                                        dialogInterface.cancel()
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getContext(), "E-mail updated successfully", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                })
                                            } else {
                                                Toast.makeText(getContext(), "Incorrect Password", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
                                }
                            }
                        })
                    }
                })
                alertDialog.show()
            }
        })
        return view
    }
}