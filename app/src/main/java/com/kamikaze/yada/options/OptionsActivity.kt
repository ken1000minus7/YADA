package com.kamikaze.yada.options

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.Task
import com.google.android.material.navigationrail.NavigationRailView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.kamikaze.yada.MainActivity
import com.kamikaze.yada.R
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.IOException

class OptionsActivity : AppCompatActivity() {
    var pfpUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        val navRail = findViewById<View>(R.id.navigation_rail) as NavigationRailView
        val rootLayout = findViewById<View>(R.id.option_layout) as ConstraintLayout
        val intent = intent
        val position = intent.getIntExtra("position", 0)
        pfpUrl = intent.getStringExtra("pfpUrl")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val item = navRail.menu.getItem(position)
        when (position) {
            1 -> {
                fragmentTransaction.replace(R.id.option_container, SecurityFragment::class.java, null).commit()
                item.isChecked = true
            }
            2 -> {
                fragmentTransaction.replace(R.id.option_container, CustomizeFragment::class.java, null).commit()
                item.isChecked = true
            }
            3 -> {
                fragmentTransaction.replace(R.id.option_container, SettingsFragment::class.java, null).commit()
                item.isChecked = true
            }
        }
        navRail.setOnItemSelectedListener { item1: MenuItem ->
            val fragmentManager1 = supportFragmentManager
            val fragmentTransaction1 = fragmentManager1.beginTransaction()
            if (navRail.selectedItemId == item1.itemId && navRail.selectedItemId != R.id.settings) return@setOnItemSelectedListener false
            fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            when (item1.itemId) {
                R.id.profile -> fragmentTransaction1.replace(R.id.option_container, ProfileFragment::class.java, null).commit()
                R.id.security -> fragmentTransaction1.replace(R.id.option_container, SecurityFragment::class.java, null).commit()
                R.id.customize -> fragmentTransaction1.replace(R.id.option_container, CustomizeFragment::class.java, null).commit()
                R.id.settings -> fragmentTransaction1.replace(R.id.option_container, SettingsFragment::class.java, null).commit()
                R.id.logout -> {
                    val confirmDialog = LayoutInflater.from(this@OptionsActivity).inflate(R.layout.confirm_dialog, rootLayout, false)
                    AlertDialog.Builder(this@OptionsActivity).setView(confirmDialog).setTitle("Are you sure you want to log out?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                        val intent1 = Intent(this@OptionsActivity, MainActivity::class.java)
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this@OptionsActivity, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        intent1.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent1)
                    }).setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(dialogInterface: DialogInterface, i: Int) {}
                    }).show()
                    return@setOnItemSelectedListener false
                }
            }
            item1.isChecked = true
            false
        }
    }

    @SuppressLint("LogNotTimber")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val aboutText = findViewById<View>(R.id.about_text) as TextView
        val nameText = findViewById<View>(R.id.name) as TextView
        val profilePic = findViewById<View>(R.id.profile_pic) as ImageView
        Log.d("datahoho", requestCode.toString())
        if (resultCode != RESULT_OK || data == null) {
            if (data == null) Log.d("stopped", "data null") else Log.d("stopped", "result cancel")
            Picasso.get().load("https://i.kym-cdn.com/photos/images/newsfeed/000/754/538/454.jpg").into(profilePic)
            nameText.text = "Dio Brando"
            aboutText.text = "You thought it was your profile pic, BUT IT WAS ME! DIO!"
            return
        }
        Log.d("lol", "didnt stop")
        when (requestCode) {
            1 -> {
                val uri = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    profilePic.setImageBitmap(bitmap)
                    updateProfilePic(uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                Log.d("gallery", data.dataString!!)
            }
            2 -> {
                val bitmap = data.extras!!["data"] as Bitmap?
                profilePic.setImageBitmap(bitmap)
                updateProfilePic(bitmap)
            }
        }
    }

    private fun updateProfilePic(uri: Uri?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating profile photo")
        progressDialog.isIndeterminate = true
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
        progressDialog.setProgressNumberFormat(null)
        progressDialog.setProgressPercentFormat(null)
        progressDialog.show()
        val firebaseStorage = FirebaseStorage.getInstance()
        val storage = firebaseStorage.getReference(FirebaseAuth.getInstance().uid + "/pfp.jpg")
        val db = FirebaseFirestore.getInstance()
        val document = db.collection("users").document(FirebaseAuth.getInstance().uid!!)
        storage.putFile(uri!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.storage.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        pfpUrl = task.result.toString()
                        document.update("imageUrl", task.result.toString()).addOnCompleteListener { task ->
                            progressDialog.cancel()
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "Profile pic updated", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else progressDialog.cancel()
                }
            } else progressDialog.cancel()
        }
    }

    private fun updateProfilePic(bitmap: Bitmap?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating profile photo")
        progressDialog.isIndeterminate = true
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.window!!.setBackgroundDrawableResource(R.drawable.empty_list_background)
        progressDialog.setProgressNumberFormat(null)
        progressDialog.setProgressPercentFormat(null)
        progressDialog.show()
        val firebaseStorage = FirebaseStorage.getInstance()
        val storage = firebaseStorage.getReference(FirebaseAuth.getInstance().uid + "/pfp.jpg")
        val db = FirebaseFirestore.getInstance()
        val document = db.collection("users").document(FirebaseAuth.getInstance().uid!!)
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val pfp = baos.toByteArray()
        storage.putBytes(pfp).addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
            if (task.isSuccessful) {
                task.result.storage.downloadUrl.addOnCompleteListener({ task1: Task<Uri> ->
                    if (task1.isSuccessful) {
                        pfpUrl = task1.result.toString()
                        document.update("imageUrl", task1.result.toString()).addOnCompleteListener({ task11: Task<Void?> ->
                            progressDialog.cancel()
                            if (task11.isSuccessful) {
                                Toast.makeText(applicationContext, "Profile pic updated", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else progressDialog.cancel()
                })
            } else progressDialog.cancel()
        }
    }

    override fun onBackPressed() {
        val aboutText = findViewById<View>(R.id.about_text) as TextView
        val aboutEdit = findViewById<View>(R.id.about_edit) as EditText
        val aboutEditButton = findViewById<View>(R.id.about_edit_img) as ImageView
        val aboutDoneButton = findViewById<View>(R.id.about_done_img) as ImageView
        val nameText = findViewById<View>(R.id.name) as TextView
        val nameEdit = findViewById<View>(R.id.name_edit) as EditText
        val nameEditButton = findViewById<View>(R.id.name_edit_img) as ImageView
        val nameDoneButton = findViewById<View>(R.id.name_done_img) as ImageView
        if ((aboutText != null && aboutText.visibility == View.INVISIBLE || nameText != null) && nameText.visibility == View.INVISIBLE) {
            aboutText.visibility = View.VISIBLE
            aboutEdit.visibility = View.INVISIBLE
            aboutDoneButton.visibility = View.INVISIBLE
            aboutEditButton.visibility = View.VISIBLE
            nameText.visibility = View.VISIBLE
            nameEdit.visibility = View.INVISIBLE
            aboutEdit.setText(aboutText.text.toString())
            nameEdit.setText(nameText.text.toString())
            nameDoneButton.visibility = View.INVISIBLE
            nameEditButton.visibility = View.VISIBLE
        } else {
            val intent = Intent()
            intent.putExtra("pfpUrl", pfpUrl)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}