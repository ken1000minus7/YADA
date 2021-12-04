package com.kamikaze.yada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.kamikaze.yada.databinding.FragmentLandingBinding
import com.kamikaze.yada.databinding.FragmentTeamKamikazeBinding

class TeamKamikaze : Fragment() {
    private var _binding: FragmentTeamKamikazeBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeamKamikazeBinding.inflate(inflater,container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        val leader = binding.leader
        leader.setOnClickListener {
            Toast.makeText(activity,"\uD83C\uDF1A \uD83E\uDD14",Toast.LENGTH_LONG).show()
        }
        val ken = binding.ken
        ken.setOnClickListener {
            Toast.makeText(activity, "\uD83D\uDC68\u200D\uD83D\uDCBB \uD83D\uDC68\u200D\uD83D\uDCBB \uD83D\uDC68\u200D\uD83D\uDCBB \uD83D\uDC68\u200D\uD83D\uDCBB ", Toast.LENGTH_LONG).show()
        }
        val rs = binding.rs
        rs.setOnClickListener {
            Toast.makeText(activity,"\uD83D\uDC68\u200D\uD83C\uDFA8 \uD83D\uDC68\u200D\uD83C\uDFA8 \uD83D\uDC68\u200D\uD83C\uDFA8 \uD83D\uDC68\u200D\uD83C\uDFA8 \u200B\uD83D\uDC68\u200D\uD83C\uDFA8\u200B\uD83C\uDFA8\u200B\uD83D\uDC69\u200D\uD83C\uDFA8\u200B", Toast.LENGTH_LONG).show()
        }
        val ashu = binding.ashu
        ashu.setOnClickListener {
            Toast.makeText(activity, "(눈_눈) ", Toast.LENGTH_LONG).show()
        }
        val priya = binding.pm
        priya.setOnClickListener {
            Toast.makeText(activity,"\uD83D\uDD2B \uD83C\uDF6D", Toast.LENGTH_LONG).show()
        }
        return view
    }


}