package com.kamikaze.yada.dao

import android.util.Log
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.errorprone.annotations.Var
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.auth.LoginActivity.Companion.TAG
import com.kamikaze.yada.diary.DiaryHandler

class NotesDao {
    val  auth : FirebaseAuth  = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val idk = db.collection("users").document(auth.currentUser!!.uid)
    public fun setNote(tv: TextView, position: Int, et: EditText ,title : TextView, gridview :GridLayout){

        idk.get().addOnCompleteListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document}")
                val dh : DiaryHandler = DiaryHandler(tv.context)
                val listofdiary = dh.convertToDiary(document.result.get("diaries") as MutableList<java.util.HashMap<String, Any>>)

               if(position<listofdiary.size) {
                   val diary = listofdiary.get(position)
                   val note = diary.note
                   val textnote = note?.textnote
                   val lis = diary.images //list of images which contains url


                   tv.text = textnote
                   et.setText(textnote)
                    val top = note?.topic

                    title.text = diary.title
               }
            } else {
                Log.d(TAG, "No such document")
            }
        }

    }
}

