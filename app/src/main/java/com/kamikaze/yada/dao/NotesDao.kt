package com.kamikaze.yada.dao

import android.app.Activity
import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.diary.DiaryHandler
import com.kamikaze.yada.diary.writenotes.ImageAdapter
import com.kamikaze.yada.diary.writenotes.WriteDiaryFragment

class NotesDao {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val idk = db.collection("users").document(auth.currentUser!!.uid)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun setNote(
        view: View,
        act: Activity,
        tv: TextView,
        position: Int,
        et: EditText,
        title: TextView,
        rv: RecyclerView,
        fragment: WriteDiaryFragment
    ) {

        idk.get().addOnCompleteListener { document ->
            if (document != null) {
                val dh = DiaryHandler(tv.context)
                val listofdiary =
                    dh.convertToDiary(document.result.get("diaries") as MutableList<HashMap<String?, Any?>>?)

                val win = act.window
                if (position < listofdiary.size) {
                    val diary = listofdiary[position]
                    if (diary != null) {
                        if (diary.color >= 0) {
                            win.statusBarColor = act.resources.getColor(diary.color)
                            view.setBackgroundColor(act.resources.getColor(diary.color))
                            fragment.currcolor = diary.color
                            win.navigationBarColor = act.resources.getColor(diary.color)
                        }
                        val note = diary.note
                        val textnote = note?.textnote
                        val lis = diary.images //list of images which contains url
                        rv.adapter = ImageAdapter(lis, rv.context)
                        if (lis?.size!! > 0) rv.visibility = View.VISIBLE
                        tv.text = textnote
                        et.setText(textnote)
                        title.text = diary.title
                    }
                }
            }
        }
    }
}

