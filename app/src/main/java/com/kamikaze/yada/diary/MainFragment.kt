package com.kamikaze.yada.diary

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kamikaze.yada.R
import com.kamikaze.yada.TranslateActivity
import com.kamikaze.yada.diary.writenotes.WriteActivity
import com.kamikaze.yada.weather.WeatherActivity
import com.kamikaze.yada.webview.BlogActivity
import java.util.*

class MainFragment : Fragment() {
    var fabOpen = false
    var floatingActionButton: FloatingActionButton? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        floatingActionButton = view.findViewById<View>(R.id.floating_button) as FloatingActionButton
        val diaryButton =
            view.findViewById<View>(R.id.floating_diary_button) as FloatingActionButton
        val translateButton =
            view.findViewById<View>(R.id.floating_translate_button) as FloatingActionButton
        val newsButton = view.findViewById<View>(R.id.floating_news_button) as FloatingActionButton
        val weatherButton =
            view.findViewById<View>(R.id.floating_weather_button) as FloatingActionButton
        floatingActionButton!!.setOnClickListener {
            if (fabOpen) {
                fabOpen = false
                diaryButton.animate().translationY(0f)
                translateButton.animate().translationY(0f)
                newsButton.animate().translationY(0f)
                weatherButton.animate().translationY(0f)
                floatingActionButton!!.setImageResource(R.drawable.ic_plus_button)
                floatingActionButton!!.setBackgroundColor(Color.WHITE)
            } else {
                fabOpen = true
                diaryButton.animate().translationY(-resources.getDimension(R.dimen._168sdp))
                translateButton.animate().translationY(-resources.getDimension(R.dimen._126sdp))
                newsButton.animate().translationY(-resources.getDimension(R.dimen._84sdp))
                weatherButton.animate().translationY(-resources.getDimension(R.dimen._44sdp))
                floatingActionButton!!.setImageResource(R.drawable.ic_close)
                floatingActionButton!!.setBackgroundColor(Color.BLACK)
            }
        }
        translateButton.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, TranslateActivity::class.java)
            startActivity(intent)
        }
        newsButton.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, BlogActivity::class.java)
            startActivity(intent)
        }
        weatherButton.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext, WeatherActivity::class.java)
            startActivity(intent)
        }
        diaryButton.setOnClickListener {
            val dialogView = inflater.inflate(R.layout.diary_input_dialog, container, false)
            val titleView = dialogView.findViewById<View>(R.id.title_input) as EditText
            val descriptionView = dialogView.findViewById<View>(R.id.description_input) as EditText
            val location = dialogView.findViewById<View>(R.id.location_input) as EditText
            val alertDialog = AlertDialog.Builder(view.context).setView(dialogView)
                .setPositiveButton("Create") { dialogInterface, i ->
                    val diaryHandler = DiaryHandler(context)
                    val diary = Diary(
                        titleView.text.toString(),
                        descriptionView.text.toString(),
                        location.text.toString()
                    )
                    val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
                    val position = recyclerView.adapter!!.itemCount
                    diaryHandler.addDiary(diary, recyclerView)
                    diaryHandler.loadData()
                    val intent1 = Intent(activity, WriteActivity::class.java)
                    intent1.putExtra("position", position)
                    intent1.putExtra("title", titleView.text.toString())
                    Log.d("intent", titleView.text.toString())
                    startActivity(intent1)
                    dialogInterface.cancel()
                }.create()
            alertDialog.window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
            alertDialog.show()
        }
        val startDiary = view.findViewById<View>(R.id.start_diary) as TextView
        val noResult = view.findViewById<View>(R.id.empty_result) as TextView
        val searchView = view.findViewById<View>(R.id.search_view) as SearchView
        val searchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        //        setupStuff(view,searchView);
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
                val diaryListRecyclerViewAdapter = recyclerView.adapter as DiaryListRecyclerViewAdapter?
                if (originalList == null) {
                    originalList = diaryListRecyclerViewAdapter!!.itemList
                }
                val result = mutableListOf<Diary?>()
                for (i in originalList!!.indices) {
                    val diary = originalList!![i]
                    if (diary != null) {
                        if (diary.location?.lowercase(Locale.getDefault())?.contains(
                                s.lowercase(
                                    Locale.getDefault()
                                )
                            )!! || diary.title?.lowercase(Locale.getDefault())?.contains(
                                s.lowercase(
                                    Locale.getDefault()
                                )
                            )!! || diary.description?.lowercase(Locale.getDefault())?.contains(
                                s.lowercase(
                                    Locale.getDefault()
                                )
                            )!!
                        ) {
                            result.add(diary)
                        }
                    }
                }
                val adapter = DiaryListRecyclerViewAdapter(context, result, recyclerView)
                recyclerView.swapAdapter(adapter, false)
                startDiary.visibility = View.INVISIBLE
                if (result.size == 0) noResult.visibility = View.VISIBLE else noResult.visibility =
                    View.INVISIBLE
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
                val diaryListRecyclerViewAdapter =
                    recyclerView.adapter as DiaryListRecyclerViewAdapter?
                if (originalList == null) {
                    originalList = diaryListRecyclerViewAdapter!!.itemList
                }
                val result = mutableListOf<Diary?>()
                for (i in originalList!!.indices) {
                    val diary = originalList!![i]
                    if (diary?.location?.lowercase(Locale.getDefault())?.contains(
                            s.lowercase(
                                Locale.getDefault()
                            )
                        )!! || diary.title?.lowercase(Locale.getDefault())?.contains(
                            s.lowercase(
                                Locale.getDefault()
                            )
                        )!! || diary.description?.lowercase(Locale.getDefault())?.contains(
                            s.lowercase(
                                Locale.getDefault()
                            )
                        )!!
                    ) {
                        result.add(diary)
                    }
                }
                val adapter = DiaryListRecyclerViewAdapter(context, result, recyclerView)
                recyclerView.swapAdapter(adapter, false)
                startDiary.visibility = View.INVISIBLE
                if (result.size == 0) noResult.visibility = View.VISIBLE else noResult.visibility =
                    View.INVISIBLE
                return false
            }
        })
        searchView.setOnCloseListener {
            if (originalList != null) {
                val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
                val adapter = DiaryListRecyclerViewAdapter(context, originalList, recyclerView)
                recyclerView.swapAdapter(adapter, false)
                if (originalList!!.size == 0) startDiary.visibility = View.VISIBLE
                originalList = null
                noResult.visibility = View.INVISIBLE
            }
            false
        }
        return view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val noResult = requireView().findViewById<View>(R.id.empty_result) as TextView
        noResult.visibility = View.INVISIBLE
    }

    companion object {
        var originalList: MutableList<Diary?>? = null
    }
}