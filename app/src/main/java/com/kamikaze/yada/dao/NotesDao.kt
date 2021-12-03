package com.kamikaze.yada.dao

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.google.errorprone.annotations.Var
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.auth.LoginActivity.Companion.TAG
import com.kamikaze.yada.diary.DiaryHandler
import com.kamikaze.yada.diary.writenotes.ImageAdapter
import com.kamikaze.yada.diary.writenotes.WriteActivity

class NotesDao {
    val  auth : FirebaseAuth  = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val idk = db.collection("users").document(auth.currentUser!!.uid)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun setNote(view:View, act: Activity, tv: TextView, position: Int, et: EditText, title : TextView, rv : RecyclerView){

        idk.get().addOnCompleteListener { document ->
            if (document != null) {
                Log.d(TAG, "DocumentSnapshot data: ${document}")
                val dh : DiaryHandler = DiaryHandler(tv.context)
                val listofdiary = dh.convertToDiary(document.result.get("diaries") as MutableList<java.util.HashMap<String, Any>>)
                //colors colors everywhere
                val win = act.window


                if(position<listofdiary.size) {
                   val diary = listofdiary[position]
                    if(diary.color >=0 ) {
                        Log.d("color ","${diary.color}")
                        win.statusBarColor = act.resources.getColor(diary.color)
                        view.setBackgroundColor(act.resources.getColor(diary.color))
                        win.navigationBarColor = act.resources.getColor(diary.color)
                        Log.d("sdad","${view.background}")
                    }
                   val note = diary.note
                    Log.d("notes","${note?.textnote}")
                   val textnote = note?.textnote
                   val lis = diary.images //list of images which contains url
                    rv.adapter = ImageAdapter(lis,rv.context)
                   if(lis.size>0) rv.visibility=View.VISIBLE
                    tv.text = textnote
                   et.setText(textnote)
                    title.text = diary.title
               }
            } else {
                Log.d(TAG, "No such document")
            }
        }

    }
}

