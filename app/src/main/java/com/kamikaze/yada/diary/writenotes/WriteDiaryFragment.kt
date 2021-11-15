package com.kamikaze.yada.diary.writenotes

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firestore.v1.Write
import com.kamikaze.yada.R
import com.kamikaze.yada.dao.NotesDao
import com.kamikaze.yada.databinding.ActivityWriteBinding
import com.kamikaze.yada.databinding.FragmentWriteDiaryBinding
import com.kamikaze.yada.diary.Diary
import com.kamikaze.yada.diary.DiaryHandler
import com.kamikaze.yada.model.Notes
import com.kamikaze.yada.diary.writenotes.WriteActivity as WriteActivity1

class WriteDiaryFragment : Fragment(R.layout.fragment_write_diary) {
    private var _binding: FragmentWriteDiaryBinding? = null
    private val binding get() = _binding!!

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

        val act = activity as com.kamikaze.yada.diary.writenotes.WriteActivity

        val nd = NotesDao()
        nd.setNote(seeTV,act.position,writeET)



        seeTV.movementMethod = ScrollingMovementMethod()
        topAppBar.setOnMenuItemClickListener { menuItem -> when (menuItem.itemId){
            R.id.favorite ->{
                writeET.visibility = View.GONE
                seeTV.text = writeET.text
                seeTV.visibility = View.VISIBLE
                val note :Notes = Notes("Random","Random","Random0",writeET.text.toString())



                val act = activity as com.kamikaze.yada.diary.writenotes.WriteActivity
                act.position
                var diaryins:DiaryHandler = DiaryHandler(activity)
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val done = menu.findItem(R.id.favorite)
        val isedit = menu.findItem(R.id.edit)
        done.setVisible(false)
        done.setOnMenuItemClickListener { menuItem ->when (menuItem.itemId){
            R.id.favorite ->{
                done.setVisible(false)
//                isedit.setVisible(true)
                true

            }
            else ->{
                false
            }

                }}
    isedit.setOnMenuItemClickListener { menuItem ->when (menuItem.itemId){
        R.id.edit ->{
        isedit.setVisible(false)
//        done.setVisible(true)
        true

    }
        else ->{
        false
    }

    }}}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}