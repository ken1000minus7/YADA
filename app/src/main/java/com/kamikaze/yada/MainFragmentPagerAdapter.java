package com.kamikaze.yada;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kamikaze.yada.diary.MainFragment;

public class MainFragmentPagerAdapter extends FragmentStateAdapter {

    public MainFragmentPagerAdapter(FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0) return new MainFragment();
        else return new FeatureFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
