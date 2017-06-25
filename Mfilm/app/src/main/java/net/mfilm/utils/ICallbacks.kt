package net.mfilm.utils

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.Toolbar
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import io.reactivex.Flowable
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.Category
import tr.xip.errorview.ErrorView

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
    fun setRefreshed(refreshed: Boolean, f: (() -> Unit)?)
    val pullToRefreshColorResources: IntArray
    fun onRefresh()
    fun initSwipe()
    val swipeContainer: SwipeRefreshLayout?
}

interface ICallbackSearchView : IBackListener {
    fun onSearch(query: String)
    var query: String?
    var category: Category?
}

interface ICallbackSearch {
    fun onSearchHistoryClicked(searchQueryRealm: SearchQueryRealm)
    fun submitSearchSuggestion(query: String)
    fun submitSearch()
    val mCallbackSearchView: ICallbackSearchView?
}
interface ICallbackToolbar : ICallbackSearch {
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
    val edtSearch: EditText
    val imgClear: IconTextView
    fun setToolbarTitle(title: String?)
    fun showBtnBack(visi: Boolean)
    fun showBtnBack()
    fun hideDrawerToggle()
    fun showOptionsMenu(show: Boolean)
    fun showDrawer()
    fun syncStateDrawer()
    fun onSearchScreenRequested()
    //    var mMenu: Menu?
//    var mToggle: ActionBarDrawerToggle?
    fun initSearch()
    fun showConfirmExit()

    fun onAbout()
    fun onSettings()
    //    fun onSearch(search: Boolean, back: Boolean=false)
    fun onSearch(search: Boolean)
    fun onShare()
    fun onFollow()

}

interface IBackListener {
    fun onBackPressed(f: (() -> Unit)?)
}

interface ICallbackErrorView {
    val errorView: ErrorView?
    val subTitle: Int?
    fun initErrorView()
    fun showErrorView(show: Boolean): Boolean
    //    fun showErrorView(show: Boolean, status_code: Int)
    fun onErrorViewDemand()

    fun onErrorViewRetry(f: () -> Unit)
    fun isDataEmpty(): Boolean = false
    fun loadFinished()
}

interface ICallbackSpanCount {
    val spanCount: Int
}

interface IBus {
    fun send(obj: Any?)
    fun asFlowable(): Flowable<Any?>
//    fun hasObservers(): Boolean
}

interface ICallbackBus {
    fun initIBus()
}
