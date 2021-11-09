package com.kamikaze.yada.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.kamikaze.yada.R
import android.widget.Toast

import com.google.firebase.auth.FirebaseUser

import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import androidx.annotation.DrawableRes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamikaze.yada.auth.LoginActivity.Companion.TAG
import com.kamikaze.yada.dao.UserDao
import com.kamikaze.yada.model.User


class NewSignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
        setContentView(R.layout.activity_new_sign_up)
        val username = findViewById<EditText>(R.id.username_email)
        val epass = findViewById<EditText>(R.id.password_email)
        val signButton = findViewById<Button>(R.id.email_new_signin)
        val newuser = findViewById<EditText>(R.id.name_one_tv)
        signButton.setOnClickListener{


            val username_text = username.text.toString()
            val epass_text = epass.text.toString()
            if (username_text.isEmpty()){
                username.error = "Please enter email"
                username.requestFocus()
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(username_text).matches()){
                username.error = "Please enter valid email"
                username.requestFocus()
            }
            if(epass_text.isEmpty()){
                epass.error = "Please enter password"
                epass.requestFocus()
            }

            auth.createUserWithEmailAndPassword(username_text, epass_text)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        var firebaseUser = task.result.user!!
                        val user = User(firebaseUser.uid , newuser.text.toString(), firebaseUser.photoUrl.toString())
                        val userDao = UserDao()
                        userDao.addUser(user)

                        Toast.makeText(
                            this, "You were registered!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this, Check::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("username", firebaseUser.uid)
                        intent.putExtra("email-id", username_text)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }


        }


    }
    override fun onStart() {
        super.onStart()

    }
    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser!= null){
            startActivity(Intent(this, Check::class.java))
        }
        else{
            Toast.makeText(baseContext , "Login Failed!", Toast.LENGTH_LONG).show()
        }
    }

}