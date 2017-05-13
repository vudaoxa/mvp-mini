package net.mfilm.ui.tabs

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_tabs.*
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.SIZE_TABS

/**
 * Created by tusi on 3/21/17.
 */
class TabsFragment : BaseStackFragment() {
    companion object {
        fun newInstance(): TabsFragment {
            val fragment = TabsFragment()
            return fragment
        }
    }

    internal var prevMenuItem: MenuItem? = null
    override fun initViews() {
        viewpager.apply {
            offscreenPageLimit = SIZE_TABS
            adapter = TabsPagerAdapter(fragmentManager, SIZE_TABS)
            navigation.setOnNavigationItemSelectedListener { item ->
                currentItem =
                        when (item.itemId) {
                            R.id.navigation_home -> 0
                            R.id.navigation_dashboard -> 1
                            else -> 2
                        }
                return@setOnNavigationItemSelectedListener false
            }
            addOnPageChangeListener(mPageChangedListener)
        }

    }

    val mPageChangedListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            navigation.apply {
                prevMenuItem?.apply {
                    isChecked = false
                } ?: let {
                    menu.getItem(0).isChecked = false
                }
                menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)
            }

        }
    }

    override fun initFields() {
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_tabs, container, false)
    }
}