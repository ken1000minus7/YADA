package com.kamikaze.yada.diary

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kamikaze.yada.R

class DiaryListFragment : Fragment() {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(context)
            val diaryHandler = DiaryHandler(getContext())
            diaryHandler.loadData(recyclerView)
            val adapter = DiaryListRecyclerViewAdapter(context, diaryHandler.diaries)
            recyclerView.adapter = adapter
            val itemTouchCallback: ItemTouchHelper.SimpleCallback =
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val confirmDialog = LayoutInflater.from(context)
                            .inflate(R.layout.confirm_dialog, container, false)
                        AlertDialog.Builder(context).setView(confirmDialog)
                            .setTitle("Are you sure you want to delete this diary?")
                            .setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                                diaryHandler.loadData()
                                diaryHandler.deleteDiary(viewHolder.position, recyclerView)
                                dialogInterface.cancel()
                            }.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
                            diaryHandler.loadData(recyclerView)
                            dialogInterface.cancel()
                        }.show()
                    }
                }
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
        return view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val recyclerView = view as RecyclerView?
        val diaryHandler = DiaryHandler(context)
        diaryHandler.loadData(recyclerView)
    }
}