package com.example.newsapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.newsapp.SearchFragment
import com.example.newsapp.TopHeadlineFragment

@Suppress("DEPRECATION")
internal class ViewPagerAdapter(var context : Context, fm : FragmentManager, var totalTabs : Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> TopHeadlineFragment()
            else -> SearchFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }

}