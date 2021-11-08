package com.kamikaze.yada.auth
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
        auth = Firebase.auth

        var username = findViewById<EditText>(R.id.username)
        var epass = findViewById<EditText>(R.id.password)
        val emailsignin = findViewById<Button>(R.id.login)


        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client))
            .requestEmail()
            .build()
        val client : GoogleSignInClient = GoogleSignIn.getClient(this ,gso)
        btnSignIn.setOnClickListener{
            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent , RC_SIGN_IN)
        }
    // Email Sign in
        emailsignin.setOnClickListener {
        val username_text = username.text.toString()
            val epass_text = epass.text.toString()
       auth.createUserWithEmailAndPassword(username_text, epass_text)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val firebaseUser = task.result!!.user!!
                        val user = User(firebaseUser.uid , firebaseUser.displayName, firebaseUser.photoUrl.toString())
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
        val user = User(firebaseuser.uid , firebaseuser.displayName, firebaseuser.photoUrl.toString())
        val userDao = UserDao()
        userDao.addUser(user)
        startActivity(Intent(this, Check::class.java))
        finish()
    }

}