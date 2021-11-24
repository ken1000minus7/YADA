package com.kamikaze.yada.diary.writenotes

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ContentView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kamikaze.yada.R
import com.kamikaze.yada.dao.NotesDao
import com.kamikaze.yada.databinding.FragmentWriteDiaryBinding
import com.kamikaze.yada.diary.DiaryHandler
import com.kamikaze.yada.model.Notes

class WriteDiaryFragment : Fragment(R.layout.fragment_write_diary) {
    private var _binding: FragmentWriteDiaryBinding? = null
    private val binding get() = _binding!!
    val images = mutableListOf<ImageView>()
    lateinit var selectedNoteColor:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        val view = binding.root
        val topAppBar = binding.topAppBar
        val writeET = binding.edithere
        writeET.visibility = View.GONE
        val seeTV = binding.seehere
        val title = binding.title
        val act = activity as WriteActivity
        topAppBar.title = act.title
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val imgview = ImageView(act)
        val layout = binding.layout
        initMisc(act.findViewById(R.id.layoutmiscnote))
        selectedNoteColor = "#e6ba9a"
        //----------------------------------------------------------
        //Button Clicks to change bg color
        val bgc1 = view.findViewById<ImageView>(R.id.imageColor1)
        val bgc2 = view.findViewById<ImageView>(R.id.imageColor2)
        val bgc3 = view.findViewById<ImageView>(R.id.imageColor3)
        val bgc4 = view.findViewById<ImageView>(R.id.imageColor4)

        bgc1.setOnClickListener {
            setBgColor(R.color.secondary)
        }
        bgc2.setOnClickListener { setBgColor(R.color.blue_light) }
        bgc3.setOnClickListener { setBgColor(R.color.orange) }
        bgc4.setOnClickListener { setBgColor(R.color.green) }
        //-----------------------------------------------------------

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
        nd.setNote(seeTV,act.position,writeET , title )
        seeTV.movementMethod = ScrollingMovementMethod()
        topAppBar.setOnMenuItemClickListener { menuItem -> when (menuItem.itemId){
            R.id.favorite ->{
                writeET.visibility = View.GONE
                seeTV.text = writeET.text
                seeTV.visibility = View.VISIBLE
                val note  = Notes(title.text.toString(),"Random","Random0",writeET.text.toString()  )
                val act = activity as WriteActivity
                val diaryins:DiaryHandler = DiaryHandler(activity)
                diaryins.updateDiary(act.position, note)
                true
            }
            R.id.edit ->{
                seeTV.visibility = View.GONE
                writeET.setText(seeTV.text.toString())
                writeET.visibility = View.VISIBLE
                true
            }
            else   -> false
        } }
        return view
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun addView(imageView : ImageView, width : Int, height : Int){

       val layoutParams = GridLayout.LayoutParams()

    imageView.scaleType = ImageView.ScaleType.CENTER_CROP


        imageView.layoutParams = layoutParams
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode==RESULT_OK){
            if(requestCode==1000){
                val returnUri = data?.data
                val act = activity as WriteActivity
                val bitmapImage = MediaStore.Images.Media.getBitmap(act.contentResolver,returnUri)
                val imgview = ImageView(act)
                imgview.setImageBitmap(bitmapImage)
                images.add(imgview)

                //local
               addView(imgview , FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT)
                val storageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().uid!!+"/images/asu.jpg")//asu= check inent me random string
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
    //----------------------------------------------------------
    //Bottom Sheet Navigation

    private  fun initMisc(layoutmisc : LinearLayout?) {
        val act = activity as WriteActivity
        if (layoutmisc!=null){
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutmisc)
        layoutmisc.setOnClickListener{
            if(bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED ){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
    }}
    }
    private fun setBgColor(value: Int){
        view?.setBackgroundResource(value)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}