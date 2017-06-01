package net.mfilm.utils

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by tusi on 5/16/17.
 */
interface ICallbackOnClick {
    fun onClick(position: Int, event: Int)
}

abstract class ALoadMore(val f: () -> Unit) {
    protected var countLoadMore: Int = 0
    //    abstract fun onLoadMore(f: ()->Unit)
    abstract fun onLoadMore()

    fun reset() {
        countLoadMore = 0
    }
}

interface ICallbackLoadMore {
    var isDataEnd: Boolean
    var page: Int
    fun onLoadMore()
    fun reset()
}

interface IAdapterLoadMore {
    //    fun onAdapterLoadMore()
    fun onAdapterLoadMore(f: () -> Unit)

    fun reset()
    //    fun onAdapterLoadMoreFinished()
    fun onAdapterLoadMoreFinished(f: () -> Unit)
}

interface ICallbackRefresh {
    fun setRefreshing(refreshing: Boolean)
    fun pullToRefreshEnabled(): Boolean
    val pullToRefreshColorResources: IntArray
    fun onRefresh()
    fun initSwipe()
    fun reset()
}

interface ICallbackToolbar {
    val optionsMenuId: Int
    val actionSettingsId: Int
    val actionAboutId: Int
    val mToolbarTitle: TextView
    val mToolbarBack: ImageButton
    val mToolbar: Toolbar
    val mDrawerLayout: DrawerLayout
    val mBtnSearch: ImageButton
    val mBtnShare: ImageButton
    val mBtnFollow: ImageButton
    val mNavView: NavigationView
    val mLayoutBtnsInfo: LinearLayout
    val mLayoutInputText: LinearLayout
    //    var mMenu: Menu?
//    var mToggle: ActionBarDrawerToggle?
    fun showConfirmExit()

    fun onAbout()
    fun onSettings()
    fun onSearch(search: Boolean)
    fun onShare()
    fun onFollow()

}