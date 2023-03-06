package com.kamikaze.yada.diary

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.R
import com.kamikaze.yada.model.Notes
import com.kamikaze.yada.model.User

class DiaryHandler(var context: Context?) {
    var currentUser: User? = null
    fun loadData(recyclerView: RecyclerView?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setMessage("Loading")
        progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
        progressDialog.show()
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(firebaseUser!!.uid).get().addOnCompleteListener { task ->
            progressDialog.cancel()
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val diaries =
                    convertToDiary(documentSnapshot["diaries"] as MutableList<HashMap<String?, Any?>>?)
                currentUser!!.diaries = diaries
                if (recyclerView!!.adapter == null) recyclerView.adapter =
                    DiaryListRecyclerViewAdapter(
                        context, diaries, recyclerView
                    ) else recyclerView.swapAdapter(
                    DiaryListRecyclerViewAdapter(
                        context, diaries, recyclerView
                    ), false
                )
            } else {
                Toast.makeText(context, "You failed, failure", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun loadData() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(firebaseUser!!.uid).get()
            .addOnCompleteListener { task: Task<DocumentSnapshot?> ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    val diaries =
                        convertToDiary(documentSnapshot?.get("diaries") as MutableList<HashMap<String?, Any?>>?)
                    currentUser!!.diaries = diaries
                } else {
                    Toast.makeText(context, "You failed, failure", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun deleteDiary(position: Int, recyclerView: RecyclerView) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("users").document(
            currentUser!!.uid
        )
        documentReference.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val diaries =
                    convertToDiary(documentSnapshot?.get("diaries") as MutableList<HashMap<String?, Any?>>?)
                diaries.removeAt(position)
                val adapter = DiaryListRecyclerViewAdapter(context, diaries, recyclerView)
                recyclerView.swapAdapter(adapter, false)
                currentUser!!.diaries = diaries
                Log.d("Size", currentUser!!.diaries.size.toString())
                documentReference.update("diaries", diaries)
                    .addOnCompleteListener { task1: Task<Void?> ->
                        if (task1.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Diary deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }

    init {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        var ok = ""
        if (firebaseUser!!.photoUrl != null) ok = firebaseUser.photoUrl.toString()
        if (currentUser == null) currentUser =
            User(firebaseUser.uid, firebaseUser.displayName, ok, mutableListOf(), "")
    }

    val diaries: MutableList<Diary?>
        get() {
            if (currentUser != null) {
                loadData()
                return currentUser!!.diaries
            }
            return mutableListOf()
        }

    fun getDiary(position: Int): Diary? //gib pos get diary
    {
        return if (currentUser != null && position < currentUser!!.diaries.size) diaries[position] else null
    }

    fun addDiary(diary: Diary?, recyclerView: RecyclerView) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("users").document(
            currentUser!!.uid
        )
        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val diaries =
                    convertToDiary(documentSnapshot["diaries"] as MutableList<HashMap<String?, Any?>>?)
                diaries.add(diary)
                currentUser!!.diaries = diaries
                val adapter = DiaryListRecyclerViewAdapter(
                    context, diaries, recyclerView
                )
                recyclerView.swapAdapter(adapter, false)
                Log.d("Size", currentUser!!.diaries.size.toString())
                documentReference.update("diaries", diaries).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            "New diary created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun updateDiary(position: Int, bgImageUrl: String?, recyclerView: RecyclerView?) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("users").document(
            currentUser!!.uid
        )
        documentReference.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val diaries =
                    convertToDiary(documentSnapshot?.get("diaries") as MutableList<HashMap<String?, Any?>>?)
                val item = diaries[position]
                if (item != null) {
                    item.bgImageUrl = bgImageUrl
                }
                diaries[position] = item
                currentUser!!.diaries = diaries
                val adapter = DiaryListRecyclerViewAdapter(context, diaries, recyclerView)
                recyclerView!!.swapAdapter(adapter, false)
                Log.d("Size", currentUser!!.diaries.size.toString())
                documentReference.update("diaries", diaries)
                    .addOnCompleteListener { task1: Task<Void?> ->
                        if (task1.isSuccessful) {
                            Log.d("Updation", "Diary bgImageUrl updated successfully")
                        }
                    }
            }
        }
    }

    fun updateDiary(position: Int, note: Notes?, color: Int) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("users").document(
            currentUser!!.uid
        )
        documentReference.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val diaries =
                    convertToDiary(documentSnapshot?.get("diaries") as MutableList<HashMap<String?, Any?>>?)
                val item = diaries[position]
                if (note == null) Log.d("note", "null") else Log.d("note", note.textnote)
                if (item != null) {
                    item.note = note
                    if (color > 0)
                        item.color = color
                }
                diaries[position] = item
                currentUser!!.diaries = diaries
                Log.d("Size", currentUser!!.diaries.size.toString())
                documentReference.update("diaries", diaries)
                        .addOnCompleteListener { task1: Task<Void?> ->
                            if (task1.isSuccessful) {
                                Log.d("xxyznote", note.toString() + "")
                                Log.d("Updation", "Diary note updated successfully")
                            }
                        }
                }
            }
        }

    fun updateDiary(position: Int, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val documentReference = db.collection("users").document(
            currentUser!!.uid
        )
        documentReference.get().addOnCompleteListener { task: Task<DocumentSnapshot?> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                val diaries =
                    convertToDiary(documentSnapshot!!.get("diaries") as MutableList<HashMap<String?, Any?>>?)
                val item = diaries[position]
                val images = item?.images
                images!!.add(imageUrl)
                item.images = images
                diaries[position] = item
                currentUser!!.diaries = diaries
                Log.d("Size", currentUser!!.diaries.size.toString())
                documentReference.update("diaries", diaries)
                    .addOnCompleteListener { task1: Task<Void?> ->
                        if (task1.isSuccessful) {
                            Log.d("Updation", "Diary images updated successfully")
                        }
                    }
            }
        }
    }

    fun convertToDiary(diaryContent: MutableList<HashMap<String?, Any?>>?): MutableList<Diary?>
    {
        val diaries = mutableListOf<Diary?>()
        if (diaryContent == null)
            return diaries
        for (i in diaryContent.indices) {
            val noteContent = diaryContent[i]["note"] as HashMap<String, String>?
            var note: Notes? = null
            var dtitle: String? = null
            var ddescription: String? = null
            var dlocation: String? = null
            var dbgImageUrl: String? = null
            var color = -1
            var dimages: MutableList<String?>? = mutableListOf()
            if (diaryContent[i]["title"] != null)
                dtitle = diaryContent[i]["title"].toString()
            if (diaryContent[i]["description"] != null)
                ddescription = diaryContent[i]["description"].toString()
            if (diaryContent[i]["location"] != null)
                dlocation = diaryContent[i]["location"].toString()
            if (diaryContent[i]["bgImageUrl"] != null)
                dbgImageUrl = diaryContent[i]["bgImageUrl"].toString()
            if (diaryContent[i]["images"] != null)
                dimages = diaryContent[i]["images"] as MutableList<String?>?
            if (diaryContent[i]["color"] != null)
                color = diaryContent[i]["color"].toString().toInt()
            if (noteContent != null)
                note = Notes(
                    noteContent["topic"]!!,
                    noteContent["description"]!!,
                    noteContent["location"]!!,
                    noteContent["textnote"]!!
                )
            diaries.add(Diary(dtitle, ddescription, dlocation, dbgImageUrl, note, color, dimages))
        }
        return diaries
    }
}