package com.kamikaze.yada

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kamikaze.yada.diary.MainFragment

class MainFragmentPagerAdapter(fragmentActivity: FragmentActivity?) : FragmentStateAdapter(fragmentActivity!!) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) MainFragment() else FeatureFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}