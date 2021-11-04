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
package com.kamikaze.yada;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kamikaze.yada.placeholder.PlaceholderContent.PlaceholderItem;
import com.kamikaze.yada.databinding.FragmentDiaryListBinding;

import org.w3c.dom.Text;

import java.util.*;

public class DiaryListRecyclerViewAdapter extends RecyclerView.Adapter<DiaryListRecyclerViewAdapter.DiaryListViewHolder>{
    public List<PlaceholderItem> itemList;
    private LayoutInflater inflater;
    public DiaryListRecyclerViewAdapter(Context context,List<PlaceholderItem> itemList)
    {
        this.itemList=itemList;
        inflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public DiaryListRecyclerViewAdapter.DiaryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.fragment_diary_list_item,parent,false);
        return new DiaryListViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListRecyclerViewAdapter.DiaryListViewHolder holder, int position) {
        PlaceholderItem item=itemList.get(position);
        holder.itemView.setText(item.content);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    class DiaryListViewHolder extends RecyclerView.ViewHolder{
        public TextView itemView;
        public DiaryListRecyclerViewAdapter adapter;
        public DiaryListViewHolder(View view,DiaryListRecyclerViewAdapter adapter)
        {
            super(view);
            itemView=(TextView) view.findViewById(R.id.content);
            this.adapter=adapter;
        }

    }
}