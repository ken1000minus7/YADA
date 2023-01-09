package com.kamikaze.yada.diary;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.kamikaze.yada.R;
import com.kamikaze.yada.diary.writenotes.WriteActivity;
import com.squareup.picasso.Picasso;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;

public class DiaryListRecyclerViewAdapter extends RecyclerView.Adapter<DiaryListRecyclerViewAdapter.DiaryListViewHolder> {
    public ArrayList<Diary> itemList;
    private final LayoutInflater inflater;
    private RecyclerView recyclerView;

    public DiaryListRecyclerViewAdapter(Context context, ArrayList<Diary> itemList) {
        this.itemList = itemList;
        inflater = LayoutInflater.from(context);
    }

    public DiaryListRecyclerViewAdapter(Context context, ArrayList<Diary> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.recyclerView = recyclerView;
        inflater = LayoutInflater.from(context);

        if (recyclerView.getParent() != null) {
            ConstraintLayout mainLayout = (ConstraintLayout) recyclerView.getParent().getParent();
            TextView startDiary = (TextView) mainLayout.findViewById(R.id.start_diary);
            if (itemList.size() == 0) startDiary.setVisibility(View.VISIBLE);
            else startDiary.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    @Override
    public DiaryListRecyclerViewAdapter.DiaryListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_diary_list_item, parent, false);

        view.setOnClickListener(view1 -> {
            int position = recyclerView.getChildLayoutPosition(view1);
            Intent intent = new Intent(recyclerView.getContext(), WriteActivity.class);
            if (MainFragment.originalList != null) {
                String title = ((TextView) view1.findViewById(R.id.title)).getText().toString();
                String description = ((TextView) view1.findViewById(R.id.description)).getText().toString();
                String location = ((TextView) view1.findViewById(R.id.location)).getText().toString();
                for (int i = 0; i < itemList.size(); i++) {
                    Diary diary = itemList.get(i);
                    if (diary.getTitle().equals(title) && diary.getLocation().equals(location) && diary.getDescription().equals(description)) {
                        position = i;
                        break;
                    }
                }
            }

            intent.putExtra("title", itemList.get(position).getTitle());
            intent.putExtra("position", position);

            recyclerView.getContext().startActivity(intent);
        });
        return new DiaryListViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryListRecyclerViewAdapter.DiaryListViewHolder holder, int position) {
        Diary item = itemList.get(position);
        holder.titleView.setText(item.getTitle());
        holder.descriptionView.setText(item.getDescription());
        holder.locationView.setText(item.getLocation());
        if (item.getLocation() != null) {
            if (item.getBgImageUrl() == null)
                new ImageGetter(item.getLocation(), holder.locationImage, item, position).execute();
            else Picasso.get().load(item.getBgImageUrl()).into(holder.locationImage);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class DiaryListViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public TextView descriptionView;
        public ImageView locationImage;
        public TextView locationView;
        public DiaryListRecyclerViewAdapter adapter;

        public DiaryListViewHolder(View view, DiaryListRecyclerViewAdapter adapter) {
            super(view);
            titleView = (TextView) view.findViewById(R.id.title);
            descriptionView = (TextView) view.findViewById(R.id.description);
            locationImage = (ImageView) view.findViewById(R.id.location_image);
            locationView = (TextView) view.findViewById(R.id.location);
            this.adapter = adapter;
        }

    }

    public class ImageGetter extends AsyncTask<Void, Void, String> {
        String location;
        ImageView locationImage;
        Diary item;
        int position;

        public ImageGetter(String location, ImageView locationImage, Diary item, int position) {
            this.location = location;
            this.locationImage = locationImage;
            this.item = item;
            this.position = position;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String searchUrl = "https://www.google.com/search?q=" + location + "+tourism+4k" + "&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiUpP35yNXiAhU1BGMBHdDeBAgQ_AUIECgB";
            String result;
            try {
                Document document = Jsoup.connect(searchUrl).get();
                Elements media = document.select("[data-src]");
                int position = new Random().nextInt(Math.min(10, media.size()));
                Element image = media.get(position);
                String imageUrl = image.attr("abs:data-src");
                result = imageUrl.replace("&quot", "");
                return result;
            } catch (Exception e) {
                e.getStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new DiaryHandler(locationImage.getContext()).updateDiary(position, s, recyclerView);
            Picasso.get().load(s).into(locationImage);
        }
    }
}