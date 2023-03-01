package com.kamikaze.yada

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.kamikaze.yada.options.OptionsActivity
import com.squareup.picasso.Picasso

class MainPageActivity : AppCompatActivity() {
    var viewPager: ViewPager2? = null
    var pfpUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        val drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val sidebar = findViewById<View>(R.id.sidebar) as NavigationView
        viewPager = findViewById<View>(R.id.main_fragment_container) as ViewPager2
        val adapter = MainFragmentPagerAdapter(this)
        viewPager!!.adapter = adapter
        viewPager!!.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                viewPager!!.isUserInputEnabled = state != ViewPager2.SCROLL_STATE_DRAGGING || viewPager!!.currentItem != 1
            }
        })
        val header = sidebar.getHeaderView(0)
        val db = FirebaseFirestore.getInstance()
        val document = db.collection("users").document(FirebaseAuth.getInstance().uid!!)
        val profilePic = header.findViewById<View>(R.id.profile_pic) as ImageView
        val nameText = header.findViewById<View>(R.id.display_name) as TextView
        document.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val imageUrl = task.result["imageUrl"] as String?
                val name = task.result["displayName"] as String?
                nameText.text = name
                if ((imageUrl != null) && imageUrl != "" && imageUrl != "null") {
                    if (profilePic != null) Picasso.get().load(imageUrl).into(profilePic) else Toast.makeText(applicationContext, "Sadge not working", Toast.LENGTH_SHORT).show()
                }
            }
        }
        sidebar.setNavigationItemSelectedListener { item: MenuItem ->
            val intent = Intent(applicationContext, OptionsActivity::class.java)
            intent.putExtra("pfpUrl", pfpUrl)
            when (item.itemId) {
                R.id.profile -> intent.putExtra("position", 0)
                R.id.security -> intent.putExtra("position", 1)
                R.id.customize -> intent.putExtra("position", 2)
                R.id.settings -> intent.putExtra("position", 3)
                R.id.logout -> {
                    val confirmDialog = LayoutInflater.from(this@MainPageActivity).inflate(R.layout.confirm_dialog, drawerLayout, false)
                    AlertDialog.Builder(this@MainPageActivity).setView(confirmDialog).setTitle("Are you sure you want to log out?").setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                        val intent = Intent(this@MainPageActivity, MainActivity::class.java)
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(this@MainPageActivity, "Logged out successfully", Toast.LENGTH_SHORT).show()
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }).setNegativeButton("No") { dialogInterface: DialogInterface?, i: Int -> }.show()
                    return@setNavigationItemSelectedListener false
                }
            }
            startActivityForResult(intent, 1)
            false
        }
    }

    override fun onBackPressed() {
        if (viewPager!!.currentItem == 0) {
            val searchView = findViewById<View>(R.id.search_view) as SearchView
            if (searchView.isIconified) super.onBackPressed() else {
                searchView.isIconified = true
                searchView.onActionViewCollapsed()
            }
        } else {
            viewPager!!.currentItem = 0
            viewPager!!.isUserInputEnabled = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && requestCode == 1) {
            val sidebar = findViewById<View>(R.id.sidebar) as NavigationView
            val header = sidebar.getHeaderView(0)
            val db = FirebaseFirestore.getInstance()
            val document = db.collection("users").document(FirebaseAuth.getInstance().uid!!)
            val profilePic = header.findViewById<View>(R.id.profile_pic) as ImageView
            val nameText = header.findViewById<View>(R.id.display_name) as TextView
            document.get().addOnCompleteListener { task: Task<DocumentSnapshot> ->
                if (task.isSuccessful) {
                    val imageUrl = task.result["imageUrl"] as String?
                    val name = task.result["displayName"] as String?
                    nameText.text = name
                    if ((imageUrl != null) && imageUrl != "" && imageUrl != "null") {
                        if (profilePic != null) Picasso.get().load(imageUrl).into(profilePic) else Toast.makeText(applicationContext, "Sadge not working", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}