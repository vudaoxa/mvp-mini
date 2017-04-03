package net.mfilm.ui.tabs

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import net.mfilm.ui.home.HomeFragment

/**
 * Created by tusi on 3/21/17.
 */
class TabsPagerAdapter(fm: FragmentManager, val size: Int) : FragmentStatePagerAdapter(fm) {
    override fun getCount() = size
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> HomeFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }
}