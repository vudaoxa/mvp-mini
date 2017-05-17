package net.mfilm.ui.home

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import net.mfilm.R
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.home.pager.HomePagerAdapter
import net.mfilm.utils.SIZE_TABS

/**
 * Created by tusi on 3/21/17.
 */
class HomePagerFragment : BaseStackFragment() {
    companion object {
        fun newInstance(): HomePagerFragment {
            val fragment = HomePagerFragment()
            return fragment
        }
    }

    internal var prevMenuItem: MenuItem? = null
    override fun initViews() {
        kotlinx.android.synthetic.main.fragment_pager_home.viewpager.apply {
            adapter = HomePagerAdapter(fragmentManager, SIZE_TABS)
            kotlinx.android.synthetic.main.fragment_pager_home.navigation.setOnNavigationItemSelectedListener { item ->
                currentItem =
                        when (item.itemId) {
                            R.id.navigation_dashboard -> 0
                            R.id.navigation_categories -> 1
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
            kotlinx.android.synthetic.main.fragment_pager_home.navigation.apply {
                prevMenuItem?.apply {
                    isChecked = false
                } ?: let {
                    menu.getItem(0).isChecked = false
                }
                menu.getItem(position).isChecked = true
                prevMenuItem = kotlinx.android.synthetic.main.fragment_pager_home.navigation.menu.getItem(position)
            }

        }
    }

    override fun initFields() {
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_pager_home, container, false)
    }
}