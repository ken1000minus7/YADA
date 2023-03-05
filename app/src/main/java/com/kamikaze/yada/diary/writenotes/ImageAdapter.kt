package com.kamikaze.yada.diary.writenotes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamikaze.yada.R

class ImageAdapter(val images: MutableList<String?>?, val context: Context) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.imginwa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val image = images?.get(position)
        Glide.with(context).load(image).into(holder.img)

        holder.img.setOnClickListener {
            WriteActivity.openZoomed(holder.img, image!!)
        }
    }

    override fun getItemCount(): Int {
        return images?.size!!
    }
}