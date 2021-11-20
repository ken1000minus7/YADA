package com.kamikaze.yada.diary.writenotes

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kamikaze.yada.R

class GridAdapter():BaseAdapter() {

    val list = mutableListOf<String>()
    override fun getCount(): Int {
return list.size    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
return position.toLong()    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.image_grid,null)
        val gl = view.findViewById<GridLayout>(R.id.gridlayoutimages)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener{
            Toast.makeText(parent.context ,"Clicked" , Toast.LENGTH_LONG).show()
            val activity = parent.context as Activity
            val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)


        }
        return view





    }

}