package com.kamikaze.yada.diary.writenotes

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.kamikaze.yada.R
import com.kamikaze.yada.dao.NotesDao
import com.kamikaze.yada.databinding.FragmentWriteDiaryBinding
import com.kamikaze.yada.diary.DiaryHandler
import com.kamikaze.yada.model.Notes
import android.view.WindowManager

class WriteDiaryFragment : Fragment(R.layout.fragment_write_diary) {
    private var _binding: FragmentWriteDiaryBinding? = null
    private val binding get() = _binding!!
    var currcolor: Int = -1
    var oldcolor: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        val view = binding.root
        val writeET = binding.edithere
        writeET.visibility = View.GONE
        val seeTV = binding.seehere
        val title = binding.diaryTitle
        val act = activity as WriteActivity
        title.text = act.title
        val fab = binding.fab
        initMisc(act.findViewById(R.id.layoutmiscnote))

        val window: Window = (activity as WriteActivity).window
        val custops = binding.customizeOptions
        val editimg = binding.editDiary
        val doneimg = binding.doneEditDiary
        //IMAGES ADAPTER
        val recyclerView = binding.rvimages
        recyclerView.layoutManager = LinearLayoutManager(act, OrientationHelper.HORIZONTAL, false)

        //Button Clicks to change bg color
        val bgc0 = view.findViewById<ImageView>(R.id.imageColor1)
        val bgc1 = view.findViewById<ImageView>(R.id.imageColor2)
        val bgc2 = view.findViewById<ImageView>(R.id.imageColor3)
        val bgc3 = view.findViewById<ImageView>(R.id.imageColor4)
        val bgc4 = view.findViewById<ImageView>(R.id.imageColor5)
        val bgc5 = view.findViewById<ImageView>(R.id.imageColor6)
        val bgc6 = view.findViewById<ImageView>(R.id.imageColor7)
        val bgc7 = view.findViewById<ImageView>(R.id.imageColor8)
        val bgc8 = view.findViewById<ImageView>(R.id.imageColor9)
        val bgc9 = view.findViewById<ImageView>(R.id.imageColor10)

        //Button Clicks to change bg theme
        val bgt0 = view.findViewById<ImageView>(R.id.imageTheme0)
        val bgt1 = view.findViewById<ImageView>(R.id.imageTheme1)
        val bgt2 = view.findViewById<ImageView>(R.id.imageTheme2)
        val bgt3 = view.findViewById<ImageView>(R.id.imageTheme3)
        val bgt4 = view.findViewById<ImageView>(R.id.imageTheme4)
        val bgt5 = view.findViewById<ImageView>(R.id.imageTheme5)
        val bgt6 = view.findViewById<ImageView>(R.id.imageTheme6)
        val bgt7 = view.findViewById<ImageView>(R.id.imageTheme7)

        //night mode toggler
        val nightModeFlags =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {

            //darkmode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                bgt0.setBackgroundResource(R.drawable.ic_tbrown)

                bgc0.setOnClickListener {
                    setBgColor(R.color.secondary)
                    window.statusBarColor = resources.getColor(R.color.secondary)
                    window.navigationBarColor = resources.getColor(R.color.secondary)
                    currcolor = R.color.secondary
                }
                bgc1.setOnClickListener {
                    setBgColor(R.color.lavender_dark1)
                    window.statusBarColor = resources.getColor(R.color.lavender_dark1)
                    window.navigationBarColor = resources.getColor(R.color.lavender_dark1)
                    currcolor = R.color.lavender_dark1

                }
                bgc2.setOnClickListener {
                    setBgColor(R.color.rose_dark2)
                    window.statusBarColor = resources.getColor(R.color.rose_dark2)
                    window.navigationBarColor = resources.getColor(R.color.rose_dark2)
                    currcolor = R.color.rose_dark2
                }
                bgc3.setOnClickListener {
                    setBgColor(R.color.pink_dark3)
                    window.statusBarColor = resources.getColor(R.color.pink_dark3)
                    window.navigationBarColor = resources.getColor(R.color.pink_dark3)
                    currcolor = R.color.pink_dark3

                }
                bgc4.setOnClickListener {
                    setBgColor(R.color.lightblue_dark4)
                    window.statusBarColor = resources.getColor(R.color.lightblue_dark4)
                    window.navigationBarColor = resources.getColor(R.color.lightblue_dark4)
                    currcolor = R.color.lightblue_dark4


                }
                bgc5.setOnClickListener {
                    setBgColor(R.color.lightgreen_dark5)
                    window.statusBarColor = resources.getColor(R.color.lightgreen_dark5)
                    window.navigationBarColor = resources.getColor(R.color.lightgreen_dark5)
                    currcolor = R.color.lightgreen_dark5
                }
                bgc6.setOnClickListener {
                    setBgColor(R.color.lightergreen_dark6)
                    window.statusBarColor = resources.getColor(R.color.lightergreen_dark6)
                    window.navigationBarColor = resources.getColor(R.color.lightergreen_dark6)
                    currcolor = R.color.lightergreen_dark6

                }
                bgc7.setOnClickListener {
                    setBgColor(R.color.purple_dark7)
                    window.statusBarColor = resources.getColor(R.color.purple_dark7)
                    window.navigationBarColor = resources.getColor(R.color.purple_dark7)
                    currcolor = R.color.purple_dark7
                }
                bgc8.setOnClickListener {
                    setBgColor(R.color.lightyellow_dark8)
                    window.statusBarColor = resources.getColor(R.color.lightyellow_dark8)
                    window.navigationBarColor = resources.getColor(R.color.lightyellow_dark8)
                    currcolor = R.color.lightyellow_dark8
                }
                bgc9.setOnClickListener {
                    setBgColor(R.color.cyan_dark9)
                    window.statusBarColor = resources.getColor(R.color.cyan_dark9)
                    window.navigationBarColor = resources.getColor(R.color.cyan_dark9)
                    currcolor = R.color.cyan_dark9
                }
                bgt0.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tbrown)
                    window.statusBarColor = resources.getColor(R.color.tbrown)
                    window.navigationBarColor = resources.getColor(R.color.tbrown)
                }
                bgt1.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tdark1)
                    window.statusBarColor = resources.getColor(R.color.tdark1)
                    window.navigationBarColor = resources.getColor(R.color.tdark1)
                }
                bgt2.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tdark2)
                    window.statusBarColor = resources.getColor(R.color.tdark2)
                    window.navigationBarColor = resources.getColor(R.color.tdark2)
                }
                bgt3.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tdark3)
                    window.statusBarColor = resources.getColor(R.color.tdark3)
                    window.navigationBarColor = resources.getColor(R.color.tdark3)
                }
                bgt4.setOnClickListener {
                    view.setBackgroundResource(R.drawable.themew)
                    window.statusBarColor = resources.getColor(R.color.rose_dark2)
                    window.navigationBarColor = resources.getColor(R.color.rose_dark2)
                }
                bgt5.setOnClickListener {
                    view.setBackgroundResource(R.drawable.themex)
                    window.statusBarColor = resources.getColor(R.color.lightyellow_light8)
                    window.navigationBarColor = resources.getColor(R.color.pink)
                }
                bgt6.setOnClickListener {
                    view.setBackgroundResource(R.drawable.themey)
                    window.statusBarColor = resources.getColor(R.color.pink)
                    window.navigationBarColor = resources.getColor(R.color.lightyellow_light8)
                }
                bgt7.setOnClickListener {
                    view.setBackgroundResource(R.drawable.themez)
                    window.statusBarColor = resources.getColor(R.color.rose_dark2)
                    window.navigationBarColor = resources.getColor(R.color.rose_dark2)
                }
            }
        } else {
            //lightmode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                //la la la color swap
                bgt0.setBackgroundResource(R.drawable.ic_tprim)


                bgc0.setOnClickListener {
                    setBgColor(R.color.secondary)
                    window.statusBarColor = resources.getColor(R.color.secondary)
                    window.navigationBarColor = resources.getColor(R.color.secondary)
                    currcolor = R.color.secondary
                }
                bgc1.setOnClickListener {
                    setBgColor(R.color.lavender_light1)
                    window.statusBarColor = resources.getColor(R.color.lavender_light1)
                    window.navigationBarColor = resources.getColor(R.color.lavender_light1)
                    currcolor = R.color.lavender_light1
                }
                bgc2.setOnClickListener {
                    setBgColor(R.color.rose_light2)
                    window.statusBarColor = resources.getColor(R.color.rose_light2)
                    window.navigationBarColor = resources.getColor(R.color.rose_light2)
                    currcolor = R.color.rose_light2
                }
                bgc3.setOnClickListener {
                    setBgColor(R.color.pink_light3)
                    window.statusBarColor = resources.getColor(R.color.pink_light3)
                    window.navigationBarColor = resources.getColor(R.color.pink_light3)
                    currcolor = R.color.pink_light3
                }
                bgc4.setOnClickListener {
                    setBgColor(R.color.lightblue_light4)
                    window.statusBarColor = resources.getColor(R.color.lightblue_light4)
                    window.navigationBarColor = resources.getColor(R.color.lightblue_light4)
                    currcolor = R.color.lightblue_light4
                }
                bgc5.setOnClickListener {
                    setBgColor(R.color.lightgreen_light5)
                    window.statusBarColor = resources.getColor(R.color.lightgreen_light5)
                    window.navigationBarColor = resources.getColor(R.color.lightgreen_light5)
                    currcolor = R.color.lightgreen_light5
                }
                bgc6.setOnClickListener {
                    setBgColor(R.color.lightergreen_light6)
                    window.statusBarColor = resources.getColor(R.color.lightergreen_light6)
                    window.navigationBarColor = resources.getColor(R.color.lightergreen_light6)
                    currcolor = R.color.lightergreen_light6
                }
                bgc7.setOnClickListener {
                    setBgColor(R.color.purple_light7)
                    window.statusBarColor = resources.getColor(R.color.purple_light7)
                    window.navigationBarColor = resources.getColor(R.color.purple_light7)
                    currcolor = R.color.purple_light7
                }
                bgc8.setOnClickListener {
                    setBgColor(R.color.lightyellow_light8)
                    window.statusBarColor = resources.getColor(R.color.lightyellow_light8)
                    window.navigationBarColor = resources.getColor(R.color.lightyellow_light8)
                    currcolor = R.color.lightyellow_light8
                }
                bgc9.setOnClickListener {
                    setBgColor(R.color.cyan_light9)
                    window.statusBarColor = resources.getColor(R.color.cyan_light9)
                    window.navigationBarColor = resources.getColor(R.color.cyan_light9)
                    currcolor = R.color.cyan_light9
                }
                bgt0.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tprim)
                    window.statusBarColor = resources.getColor(R.color.tprim)
                    window.navigationBarColor = resources.getColor(R.color.tprim)
                }
                bgt1.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tlight1)
                    window.statusBarColor = resources.getColor(R.color.tlight1)
                    window.navigationBarColor = resources.getColor(R.color.tlight1)
                }
                bgt2.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tlight2)
                    window.statusBarColor = resources.getColor(R.color.tlight2)
                    window.navigationBarColor = resources.getColor(R.color.tlight2)
                }
                bgt3.setOnClickListener {
                    view.setBackgroundResource(R.drawable.tlight3)
                    window.statusBarColor = resources.getColor(R.color.tlight3)
                    window.navigationBarColor = resources.getColor(R.color.tlight3)
                }
            }
            bgt4.setOnClickListener {
                view.setBackgroundResource(R.drawable.themew)
                window.statusBarColor = resources.getColor(R.color.rose_dark2)
                window.navigationBarColor = resources.getColor(R.color.rose_dark2)
            }
            bgt5.setOnClickListener {
                view.setBackgroundResource(R.drawable.themex)
                window.statusBarColor = resources.getColor(R.color.lightyellow_light8)
                window.navigationBarColor = resources.getColor(R.color.pink)
            }
            bgt6.setOnClickListener {
                view.setBackgroundResource(R.drawable.themey)
                window.statusBarColor = resources.getColor(R.color.pink)
                window.navigationBarColor = resources.getColor(R.color.lightyellow_light8)
            }
            bgt7.setOnClickListener {
                view.setBackgroundResource(R.drawable.themez)
                window.statusBarColor = resources.getColor(R.color.rose_dark2)
                window.navigationBarColor = resources.getColor(R.color.rose_dark2)
            }
        }

        fab.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    act,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 2000)
            } else {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1000)
            }
        }
        val nd = NotesDao()
        nd.setNote(view, act, seeTV, act.position, writeET, title, recyclerView, this)

        window.navigationBarColor = resources.getColor(R.color.black)
        seeTV.movementMethod = ScrollingMovementMethod()
        editimg.setOnClickListener {
            window.navigationBarColor = resources.getColor(R.color.black)
            seeTV.visibility = View.GONE
            writeET.setText(seeTV.text.toString())
            writeET.visibility = View.VISIBLE
            custops.root.visibility = View.VISIBLE
            editimg.visibility = View.GONE
            doneimg.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            oldcolor = currcolor
        }
        doneimg.setOnClickListener {
            writeET.visibility = View.GONE
            val textstuff = writeET.text.toString()
            seeTV.text = textstuff
            handleKeyEvent(view, KeyEvent.KEYCODE_ENTER)
            seeTV.visibility = View.VISIBLE
            custops.root.visibility = View.GONE
            editimg.visibility = View.VISIBLE
            doneimg.visibility = View.GONE
            fab.visibility = View.GONE
            val bottomSheetBehavior = BottomSheetBehavior.from(custops.root)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) bottomSheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
            val note = Notes(title.text.toString(), "Random", "Random0", textstuff)
            val act = activity as WriteActivity
            val diaryins = DiaryHandler(activity)

            oldcolor = -1
            diaryins.updateDiary(act.position, note, currcolor)
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                val act = activity as WriteActivity
                val recyclerView = view?.findViewById<RecyclerView>(R.id.rvimages)
                val returnUri = data?.data
                val path1 = returnUri.toString()

                val imgadapter = recyclerView?.adapter as ImageAdapter?

                var imagepath = imgadapter?.images
                if (imagepath == null) imagepath = mutableListOf()
                imagepath.add(path1)
                loadImages(imagepath)

                //local
                val storageRef = FirebaseStorage.getInstance()
                    .getReference(FirebaseAuth.getInstance().uid!! + "/images/" + randomString(5) + ".jpg")
                storageRef.putFile(returnUri!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.storage.downloadUrl.addOnCompleteListener { task ->
                            val url = task.result.toString()
                            val diaryobj = DiaryHandler(act)
                            diaryobj.updateDiary(act.position, url)
                        }
                    }
                }
            }
        }
    }

    private fun randomString(i: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return List(i) { charset.random() }
            .joinToString("")
    }

    private fun initMisc(layoutmisc: LinearLayout?) {
        if (layoutmisc != null) {
            val bottomSheetBehavior = BottomSheetBehavior.from(layoutmisc)
            layoutmisc.setOnClickListener {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    val fab = view?.findViewById<FloatingActionButton>(R.id.fab)
                    fab?.visibility = View.GONE
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    val fab = view?.findViewById<FloatingActionButton>(R.id.fab)

                    fab?.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun setBgColor(value: Int) {
        view?.setBackgroundResource(value)
    }

    @SuppressLint("WrongConstant")
    fun loadImages(imagepath: MutableList<String?>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvimages)
        val act = activity as WriteActivity

        recyclerView?.layoutManager = LinearLayoutManager(act, OrientationHelper.HORIZONTAL, false)
        recyclerView?.adapter = ImageAdapter(imagepath, act)
        if (imagepath.isNotEmpty()) recyclerView?.visibility = View.VISIBLE
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            //Hide Keyboard
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            inputMethodManager.showSoftInput(view, 0)
            return true
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun restoreBg() {
        if (oldcolor > 0) setBgColor(oldcolor)
        currcolor = -1
    }
}