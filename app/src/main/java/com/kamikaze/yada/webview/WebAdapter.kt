package com.kamikaze.yada.webview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kamikaze.yada.R

class WebAdapter(val context: Context,
                 val people :HashMap<String,String>,
                 val peoplekey: ArrayList<String>) :
    RecyclerView.Adapter<WebAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<CardView>(R.id.webcard)
        val tit = view.findViewById<TextView>(R.id.titleweb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.web_items, parent, false)
        return ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tit.text = peoplekey[position]
        holder.card.setOnClickListener {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", people[peoplekey[position]])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return people.size
    }
}