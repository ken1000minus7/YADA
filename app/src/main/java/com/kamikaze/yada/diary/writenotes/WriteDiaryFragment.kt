package com.kamikaze.yada.diary.writenotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.kamikaze.yada.R
import com.kamikaze.yada.databinding.FragmentLandingBinding
import com.kamikaze.yada.diary.writenotes.WriteActivity
import com.kamikaze.yada.databinding.FragmentWriteDiaryBinding

class WriteDiaryFragment : Fragment() {
    private var _binding: FragmentWriteDiaryBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteDiaryBinding.inflate(inflater, container, false)
        val view = binding.root

        val writeNoteET : EditText = binding.writehere
        writeNoteET.setVisibility(View.GONE)
        val seeNoteTV : TextView = binding.seehere

        val writeNote = writeNoteET.text.toString()

        switchTVET.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                seeNoteTV.setVisibility(View.GONE)
            writeNoteET.setVisibility(View.VISIBLE)

            }
            else{
                seeNoteTV.text = writeNoteET.text
                seeNoteTV.setVisibility(View.VISIBLE)
                writeNoteET.setVisibility(View.GONE)
            }
        }






        return view
    }

    override fun onPause() {
        super.onPause()
        binding.seehere.text=binding.writehere.text.toString()
    }
}