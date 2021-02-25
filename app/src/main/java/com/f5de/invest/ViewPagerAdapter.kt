package com.f5de.invest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentItems = ArrayList<Fragment>()
    private val mFragmentTitles = ArrayList<String>()

    //we need to create function to add fragments

    fun addFragment(fragmentItem: Fragment, fragmentTitle: String) {
        mFragmentItems.add(fragmentItem)
        mFragmentTitles.add(fragmentTitle)
    }

    fun removeFragment(fragmentItem: Fragment, fragmentTitle: String) {
        mFragmentItems.remove(fragmentItem)
        mFragmentTitles.remove(fragmentTitle)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentItems[position]
    }

    override fun getCount(): Int {
        return mFragmentItems.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitles[position]
    }
}