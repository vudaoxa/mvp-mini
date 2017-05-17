package net.mfilm.ui.home.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import net.mfilm.ui.categories.CategoriesFragment
import net.mfilm.ui.mangas.MangasFragment

/**
 * Created by tusi on 3/21/17.
 */
class HomePagerAdapter(fm: FragmentManager, val size: Int) : FragmentStatePagerAdapter(fm) {
    override fun getCount() = size
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MangasFragment.newInstance()
            1 -> CategoriesFragment.newInstance()
            else -> MangasFragment.newInstance()
        }
    }
}