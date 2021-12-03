package com.kamikaze.yada.diary.writenotes

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamikaze.yada.MainPageActivity
import com.kamikaze.yada.R


class ImageAdapter(val images:List<String>, val context: Context) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.imginwa)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
val view : View = LayoutInflater.from(parent.context).inflate(R.layout.image_rv , parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {

        val image = images[position]
        Glide.with(context).load(image).into(holder.img)

        holder.img.setOnClickListener {
//            val intent = Intent(context , ZoomImageActivity::class.java)
//        intent.putExtra("image" , image)
//            Log.d("image", "$image , $images"  )
//            context.startActivity(intent)
            WriteActivity.openZoomed(holder.img,image)
                }
    }

    override fun getItemCount(): Int {
return images.size
    }
}