//package com.kamikaze.yada;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.kamikaze.yada.placeholder.PlaceholderContent.PlaceholderItem;
//import com.kamikaze.yada.databinding.FragmentDiaryListBinding;
//
//import java.util.List;
//
///**
// * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
// * TODO: Replace the implementation with code for your data type.
// */
//public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
//
//    private final List<PlaceholderItem> mValues;
//
//    public MyItemRecyclerViewAdapter(List<PlaceholderItem> items) {
//        mValues = items;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        return new ViewHolder(FragmentDiaryListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
//
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);
//    }
//
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final TextView mIdView;
//        public final TextView mContentView;
//        public PlaceholderItem mItem;
//
//        public ViewHolder(FragmentDiaryListBinding binding) {
//            super(binding.getRoot());
//            mIdView = binding.itemNumber;
//            mContentView = binding.content;
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
//    }
//}
package com.kamikaze.yada.diary;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kamikaze.yada.MainPageActivity;
import com.kamikaze.yada.R;
import com.kamikaze.yada.diary.writenotes.WriteActivity;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class DiaryListRecyclerViewAdapter extends RecyclerView.Adapter<DiaryListRecyclerViewAdapter.DiaryListViewHolder>{
    public ArrayList<Diary> itemList;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    public DiaryListRecyclerViewAdapter(Context context,ArrayList<Diary> itemList)
    {
        this.itemList=itemList;
        inflater=LayoutInflater.from(context);
    }
    public DiaryListRecyclerViewAdapter(Context context,ArrayList<Diary> itemList,RecyclerView recyclerView)
    {
        this.itemList=itemList;
        this.recyclerView=recyclerView;
        inflater=LayoutInflater.from(context);
        ConstraintLayout mainLayout= (ConstraintLayout) recyclerView.getParent().getParent();
        TextView startDiary=(TextView) mainLayout.findViewById(R.id.start_diary);
        if(itemList.size()==0) startDiary.setVisibility(View.VISIBLE);
        else startDiary.setVisibility(View.INVISIBLE);
        Log.d("visibility", String.valueOf(startDiary.getVisibility()));
        Log.d("hello",recyclerView.toString());
    }
    @NonNull
    @Override
    public DiaryListRecyclerViewAdapter.DiaryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.fragment_diary_list_item,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=recyclerView.getChildLayoutPosition(view);
                Intent intent=new Intent(recyclerView.getContext(), WriteActivity.class);
                intent.putExtra("title",itemList.get(position).getTitle());
                intent.putExtra("position",position);
                recyclerView.getContext().startActivity(intent);
            }
        });
        return new DiaryListViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListRecyclerViewAdapter.DiaryListViewHolder holder, int position) {
        Diary item=itemList.get(position);
        holder.titleView.setText(item.getTitle());
        holder.descriptionView.setText(item.getDescription());
        if(item.getLocation()!=null)
        {
            if( item.getBgImageUrl()==null) new ImageGetter(item.getLocation(),holder.locationImage,item,position).execute();
            else Picasso.get().load(item.getBgImageUrl()).into(holder.locationImage);
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    class DiaryListViewHolder extends RecyclerView.ViewHolder{
        public TextView titleView;
        public TextView descriptionView;
        public ImageView locationImage;
        public DiaryListRecyclerViewAdapter adapter;
        public DiaryListViewHolder(View view,DiaryListRecyclerViewAdapter adapter)
        {
            super(view);
            titleView=(TextView) view.findViewById(R.id.title);
            descriptionView=(TextView) view.findViewById(R.id.description);
            locationImage=(ImageView) view.findViewById(R.id.location_image);
            this.adapter=adapter;
        }

    }
    public class ImageGetter extends AsyncTask<Void,Void,String>
    {
        String location;
        ImageView locationImage;
        Diary item;
        int position;
        public ImageGetter(String location,ImageView locationImage,Diary item,int position)
        {
            this.location=location;
            this.locationImage=locationImage;
            this.item=item;
            this.position=position;
        }
        @Override
        protected String doInBackground(Void... voids) {
            String searchUrl= "https://www.google.com/search?q=" + location + "+tourism+4k"+ "&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiUpP35yNXiAhU1BGMBHdDeBAgQ_AUIECgB";
//            String searchUrl="https://api.unsplash.com/search/photos?query="+location+"+tourism&client_id=6fa91622109e859b1c40218a5dead99f7262cf4f698b1e2cb89dd18fc5824d15";
            Log.d("searchy",searchUrl);
            String result=null;
            try
            {
                Document document=Jsoup.connect(searchUrl).get();
                String siteResponse=document.toString();
                Elements media=document.select("[data-src]");
                int position= new Random().nextInt(Math.min(10, media.size()));
                Element image= media.get(position);
                String imageUrl=image.attr("abs:data-src");
                result =imageUrl.replace("&quot", "");
                Log.d("locatedvai",result);
                return result;
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new DiaryHandler(locationImage.getContext()).updateDiary(position,s,recyclerView);
            Picasso.get().load(s).into(locationImage);
        }
    }
}