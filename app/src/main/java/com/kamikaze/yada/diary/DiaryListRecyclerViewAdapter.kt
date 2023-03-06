package com.kamikaze.yada.diary

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.kamikaze.yada.R
import com.kamikaze.yada.diary.DiaryListRecyclerViewAdapter.DiaryListViewHolder
import com.kamikaze.yada.diary.writenotes.WriteActivity
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import java.util.*

class DiaryListRecyclerViewAdapter : RecyclerView.Adapter<DiaryListViewHolder> {
    var itemList: MutableList<Diary?>?
    private val inflater: LayoutInflater
    private var recyclerView: RecyclerView? = null

    constructor(context: Context?, itemList: MutableList<Diary?>?) {
        this.itemList = itemList
        inflater = LayoutInflater.from(context)
    }

    constructor(context: Context?, itemList: MutableList<Diary?>?, recyclerView: RecyclerView?) {
        this.itemList = itemList
        this.recyclerView = recyclerView
        inflater = LayoutInflater.from(context)
        if (recyclerView!!.parent != null) {
            val mainLayout = recyclerView.parent.parent as ConstraintLayout
            val startDiary = mainLayout.findViewById<View>(R.id.start_diary) as TextView
            if (itemList!!.size == 0) startDiary.visibility =
                View.VISIBLE else startDiary.visibility = View.INVISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryListViewHolder {
        val view = inflater.inflate(R.layout.fragment_diary_list_item, parent, false)
        view.setOnClickListener { view1: View ->
            var position = recyclerView!!.getChildLayoutPosition(view1)
            val intent = Intent(recyclerView!!.context, WriteActivity::class.java)
            if (MainFragment.originalList != null) {
                val title = (view1.findViewById<View>(R.id.title) as TextView).text.toString()
                val description =
                    (view1.findViewById<View>(R.id.description) as TextView).text.toString()
                val location = (view1.findViewById<View>(R.id.location) as TextView).text.toString()
                for (i in itemList!!.indices) {
                    val diary = itemList!![i]
                    if (diary?.title == title && diary.location == location && diary.description == description) {
                        position = i
                        break
                    }
                }
            }
            intent.putExtra("title", itemList!![position]?.title)
            intent.putExtra("position", position)
            recyclerView!!.context.startActivity(intent)
        }
        return DiaryListViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: DiaryListViewHolder, position: Int) {
        val item = itemList!![position]
        if (item != null) {
            holder.titleView.text = item.title
        }
        holder.descriptionView.text = item?.description
        holder.locationView.text = item?.location
        if (item?.location != null) {
            if (item.bgImageUrl == null)
                ImageGetter(
                    item.location!!,
                    holder.locationImage,
                    item,
                    position
                ).execute() else Picasso.get().load(item.bgImageUrl).into(holder.locationImage)
        }
    }

    override fun getItemCount(): Int {
        return itemList!!.size
    }

    inner class DiaryListViewHolder(view: View, adapter: DiaryListRecyclerViewAdapter) :
        RecyclerView.ViewHolder(view) {
        var titleView: TextView
        var descriptionView: TextView
        var locationImage: ImageView
        var locationView: TextView
        var adapter: DiaryListRecyclerViewAdapter

        init {
            titleView = view.findViewById<View>(R.id.title) as TextView
            descriptionView = view.findViewById<View>(R.id.description) as TextView
            locationImage = view.findViewById<View>(R.id.location_image) as ImageView
            locationView = view.findViewById<View>(R.id.location) as TextView
            this.adapter = adapter
        }
    }

    inner class ImageGetter(
        var location: String,
        var locationImage: ImageView,
        var item: Diary,
        var position: Int
    ) : AsyncTask<Void?, Void?, String?>() {
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            DiaryHandler(locationImage.context).updateDiary(position, s, recyclerView)
            Picasso.get().load(s).into(locationImage)
        }

        override fun doInBackground(vararg p0: Void?): String? {
            val searchUrl =
                "https://www.google.com/search?q=$location+tourism+4k&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiUpP35yNXiAhU1BGMBHdDeBAgQ_AUIECgB"
            val result: String
            try {
                val document = Jsoup.connect(searchUrl).get()
                val media = document.select("[data-src]")
                val position = Random().nextInt(Math.min(10, media.size))
                val image = media[position]
                val imageUrl = image.attr("abs:data-src")
                result = imageUrl.replace("&quot", "")
                return result
            } catch (e: Exception) {
                e.stackTrace
            }
            return null
        }
    }
}