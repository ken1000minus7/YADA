package com.kamikaze.yada.dao

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.kamikaze.yada.diary.Diary
import com.kamikaze.yada.model.User

class NotesDao {
    private val db = FirebaseFirestore.getInstance()
    private val notescollection = db.collection("users").document("notes")

}

fun updateNotes(user: User, diary: Diary){

}
