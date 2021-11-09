package com.kamikaze.yada.auth
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kamikaze.yada.MainPageActivity
import com.kamikaze.yada.R
import com.kamikaze.yada.dao.UserDao
import com.kamikaze.yada.model.User

class LoginActivity : AppCompatActivity() {
    companion object {
      private const val  RC_SIGN_IN = 123
        const val TAG = "LoginActivity"
    }
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var username = findViewById<EditText>(R.id.username)
        var epass = findViewById<EditText>(R.id.password)
        val emailsignin = findViewById<Button>(R.id.login)
        val emailbutton = findViewById<Button>(R.id.login_again)


        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        auth = Firebase.auth


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client))
            .requestEmail()
            .build()
        val client : GoogleSignInClient = GoogleSignIn.getClient(this ,gso)
        btnSignIn.setOnClickListener{

            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent , RC_SIGN_IN)
        }

        //login_again with email and password.
        emailbutton.setOnClickListener{

            val emailnewpage = Intent(this , NewSignUp::class.java)
            startActivity(emailnewpage)
        }

    // Email Sign in

        emailsignin.setOnClickListener {


            val username_text = username.text.toString()
            val epass_text = epass.text.toString()
           connectuser(username_text , epass_text ,username , epass)
        }
    }

    private fun connectuser(username_text: String, epass_text: String, username: EditText , epass: EditText) {

        if (username_text.isEmpty()){
            username.error = "Please enter email"
            username.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(username_text).matches()){
            username.error = "Please enter valid email"
            username.requestFocus()
            return
        }
        if(epass_text.isEmpty()){
            epass.error = "Please enter password"
            epass.requestFocus()
            return
        }




        auth.signInWithEmailAndPassword(username_text, epass_text)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")


                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }}

        private fun firebaseAuthWithGoogle(idToken: String) {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser


        updateUI(currentUser)
    }
    private fun updateUI(firebaseuser: FirebaseUser?) {
        //Navigate to MainActivity
        if (firebaseuser == null){
            Log.w(TAG, "user is null , not going to navigate")
            return
        }

        startActivity(Intent(this, MainPageActivity::class.java))
        finish()
    }

}