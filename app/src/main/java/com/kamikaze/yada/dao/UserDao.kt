package com.kamikaze.yada.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.model.User
class UserDao {
    private val db = FirebaseFirestore.getInstance()

    fun addUser(user: User?) {
        if (user != null) {
            db.collection("users")
                .document(user.uid)
                .set(user)
        }
    }
}

