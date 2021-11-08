package com.kamikaze.yada.dao

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.auth.LoginActivity.Companion.TAG
import com.kamikaze.yada.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")


    fun addUser(user: User?){
        if (user != null) {
            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener{e ->
                    Log.w(TAG, "Error adding document", e)

                }
        }
        }
    }

