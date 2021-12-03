package com.kamikaze.yada.diary.writenotes

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.kamikaze.yada.MemorablePlacesActivity
import com.kamikaze.yada.R
import com.kamikaze.yada.dao.NotesDao
import com.kamikaze.yada.databinding.FragmentWriteDiaryBinding
import com.kamikaze.yada.diary.Diary
import com.kamikaze.yada.diary.DiaryHandler
import com.kamikaze.yada.model.Notes
import java.lang.StringBuilder
import android.view.WindowManager
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat









class WriteDiaryFragment : Fragment(R.layout.fragment_write_diary) {
    private var _binding: FragmentWriteDiaryBinding? = null
    private val binding get() = _binding!!
     var i : Int = 0
    val a=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    //@RequiresApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        val view = binding.root
//        val topAppBar = binding.topAppBar
        val writeET = binding.edithere
        writeET.visibility = View.GONE
        val seeTV = binding.seehere
        val title = binding.diaryTitle
        val act = activity as WriteActivity
//        topAppBar.title = act.title
        title.text=act.title
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        initMisc(act.findViewById(R.id.layoutmiscnote))

        val custops=view.findViewById<View>(R.id.customize_options)
        val editimg=view.findViewById<ImageView>(R.id.edit_diary)
        val doneimg=view.findViewById<ImageView>(R.id.done_edit_diary)
        //IMAGES ADAPTER---------------------------------------------------------------------
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvimages)
        recyclerView.layoutManager = LinearLayoutManager(act , OrientationHelper.HORIZONTAL , false)


        //----------------------------------------------------------
        // Colors ,Colors everywhere
            var currcolor =R.color.secondary

            val window: Window = (activity as WriteActivity).window

        //window.statusBarColor = Color.BLUE

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
        val bgt1 = view.findViewById<ImageView>(R.id.imageTheme1)
        val bgt2 = view.findViewById<ImageView>(R.id.imageTheme2)
        val bgt3 = view.findViewById<ImageView>(R.id.imageTheme3)
        val bgt4 = view.findViewById<ImageView>(R.id.imageTheme4)
        val bgt5 = view.findViewById<ImageView>(R.id.imageTheme5)
        val bgt6 = view.findViewById<ImageView>(R.id.imageTheme6)
        //night mode toggler
        val nightModeFlags =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {

            //darkmode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("build what", "is it working")
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                //la la some colors hahahha
           //     bgt1.setImageResource(R.drawable.ic_tbrown)

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
                bgt1.setOnClickListener { view.setBackgroundResource(R.drawable.tbrown) }
                bgt2.setOnClickListener { view.setBackgroundResource(R.drawable.tdark1) }
                bgt3.setOnClickListener { view.setBackgroundResource(R.drawable.tdark2) }
                bgt4.setOnClickListener { view.setBackgroundResource(R.drawable.tdark3) }
            }
        }
        else{
            //lightmode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("build what", "is it working")
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

                bgc0.setOnClickListener {
                    setBgColor(R.color.secondary)
                   window.statusBarColor=resources.getColor(R.color.secondary)
                    window.navigationBarColor=resources.getColor(R.color.secondary)
                    currcolor = R.color.secondary
                }
                bgc1.setOnClickListener {
                    setBgColor(R.color.lavender_light1)
                    window.statusBarColor=resources.getColor(R.color.lavender_light1)
                    window.navigationBarColor=resources.getColor(R.color.lavender_light1)
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
                bgt1.setOnClickListener { view.setBackgroundResource(R.drawable.tprim)
                    Log.d("hehe","brr")}
                bgt2.setOnClickListener { view.setBackgroundResource(R.drawable.tlight1) }
                bgt3.setOnClickListener { view.setBackgroundResource(R.drawable.tlight2) }
                bgt4.setOnClickListener { view.setBackgroundResource(R.drawable.tlight3) }
            }

        }



        //-----------------------------------------------------------
        //------------------------------------------------------------

        fab.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(act,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2000)
            }
            else{
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent , "Select Picture"),1000)
            }
    }
        //-------------------------------------------------------------
        val nd = NotesDao()
        nd.setNote(view, act,seeTV,act.position,writeET , title , recyclerView)



            window.navigationBarColor = resources.getColor(R.color.black)
        seeTV.movementMethod = ScrollingMovementMethod()
//        topAppBar.setOnMenuItemClickListener { menuItem -> when (menuItem.itemId){
//            R.id.favorite ->{
//                writeET.visibility = View.GONE
//                seeTV.text = writeET.text
//                      handleKeyEvent(view , KeyEvent.KEYCODE_ENTER)
//                seeTV.visibility = View.VISIBLE
//                custops.visibility=View.GONE
//                val note  = Notes(title.text.toString(),"Random","Random0",writeET.text.toString()  )
//                val act = activity as WriteActivity
//                val diaryins:DiaryHandler = DiaryHandler(activity)
//                diaryins.updateDiary(act.position, note)
//                true
//            }
//            R.id.edit ->{
//                writeET.isSelected = true
//                val inputMethodManager =
//                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


//                seeTV.visibility = View.GONE
//                writeET.setText(seeTV.text.toString())
//                writeET.visibility = View.VISIBLE
//                custops.visibility=View.VISIBLE
//                true
//            }
//            else   -> false
//        } }
        editimg.setOnClickListener(View.OnClickListener {
            window.navigationBarColor=resources.getColor(R.color.black)

            seeTV.visibility = View.GONE
            writeET.setText(seeTV.text.toString())
            writeET.visibility = View.VISIBLE
            custops.visibility=View.VISIBLE
            editimg.visibility=View.GONE
            doneimg.visibility=View.VISIBLE
            fab.visibility=View.VISIBLE
        })
        doneimg.setOnClickListener(View.OnClickListener {
            writeET.visibility = View.GONE
            seeTV.text = writeET.text
            handleKeyEvent(view , KeyEvent.KEYCODE_ENTER)
            seeTV.visibility = View.VISIBLE
            custops.visibility=View.GONE
            editimg.visibility=View.VISIBLE
            doneimg.visibility=View.GONE
            fab.visibility=View.GONE
            val note  = Notes(title.text.toString(),"Random","Random0",writeET.text.toString())
            val act = activity as WriteActivity
            val diaryins:DiaryHandler = DiaryHandler(activity)
            diaryins.updateDiary(act.position, note)
            Log.d("xyznote","${note.textnote}")
        //    diaryins.updateDiary(act.position ,currcolor,R.color.cyan_light9 )
            Log.d("oof clor","$currcolor")
        })
        return view
    }
    //Setting Images in Recycler View------------------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode==RESULT_OK){
            if(requestCode==1000){
                val act = activity as WriteActivity
                val recyclerView = view?.findViewById<RecyclerView>(R.id.rvimages)
                val returnUri = data?.data
                val path1 = returnUri.toString()

                val imgadapter = recyclerView?.adapter  as ImageAdapter?

                var imagepath = imgadapter?.images as MutableList?
                if(imagepath==null) imagepath= mutableListOf<String>()
                imagepath.add(path1)
                loadImages(imagepath)

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                //local
                val storageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().uid!!+"/images/"+ randomString(5)+".jpg")//asu= check intent me random string
                storageRef.putFile(returnUri!!).addOnCompleteListener{
                    task->
                        if (task.isSuccessful) {
                            task.result.storage.downloadUrl.addOnCompleteListener { task ->
                                    val url = task.result.toString()
                                    val diaryobj = DiaryHandler(act)
                                    diaryobj.updateDiary(act.position , url)
                            }
                        }
                }
            }
        }
    }

    private fun randomString(i: Int): String {
    val characters = "abcdefghijklmnopqrstuvwxyz"
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return List(i) { charset.random() }
            .joinToString("")
    }

    //----------------------------------------------------------
    //Bottom Sheet Navigation

    private  fun initMisc(layoutmisc : LinearLayout?) {
        if (layoutmisc!=null){
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutmisc)
        layoutmisc.setOnClickListener{
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED ){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                val fab = view?.findViewById<FloatingActionButton>(R.id.fab)
                    fab?.visibility = View.GONE
            }
            else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                val fab = view?.findViewById<FloatingActionButton>(R.id.fab)

                    fab?.visibility = View.VISIBLE

            }
    }}
    }
    private fun setBgColor(value: Int){
        view?.setBackgroundResource(value)
    }


    @SuppressLint("WrongConstant")
    fun loadImages(imagepath :List<String>){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvimages)
        val act = activity as WriteActivity

        recyclerView?.layoutManager = LinearLayoutManager(act , OrientationHelper.HORIZONTAL , false)
        recyclerView?.adapter = ImageAdapter(imagepath,act)
        if(imagepath.size>0) recyclerView?.visibility=View.VISIBLE

    }
    private fun handleKeyEvent(view: View, keyCode: Int ):Boolean{
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            //Hide Keyboard
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0 )
            inputMethodManager.showSoftInput(view,0)
            return true


        }
        return false
    }



override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}