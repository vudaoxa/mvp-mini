package net.mfilm.utils

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.joanzapata.iconify.widget.IconTextView
import io.reactivex.Flowable
import io.realm.RealmObject
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.Category
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.custom.SimpleViewAnimator
import net.mfilm.ui.custom.SwitchButtons
import net.mfilm.ui.dialog_menus.DialogMenuAdapter
import net.mfilm.ui.manga.*
import net.mfilm.ui.manga.Filter
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
import tr.xip.errorview.ErrorView

/**
 * Created by tusi on 5/16/17.
 */
interface ICallbackPageChange {
    var currentPage: Int
    fun initPageChange()
}

interface ICallbackViewContinue {
    fun initBtnViewContinue()
    fun showBtnViewContinue()
}

interface ICallbackRvFailure {
    fun onRvFailure(position: Int)
}

interface ICallback
interface ICallbackPagerBtns {
    val layoutPagerBtns: View
    var pagerBtns: SwitchButtons?
    var pagerPosition: Int
    val btns: List<View>
    fun initPagerBtns()
    fun onPagerSelected(i: Int)
}

interface ICallbackMangaSources {
    val mMangaSources: MangaSources?
}

interface ICallbackMangaSourcesHolder {
    var mMangaSources: MangaSources?
}
interface ISwitch {
    val size: Int
    fun onSwitch(i: Int)
    fun setOnClickListener(f: (Int) -> Unit)
    fun onClick(i: Int)
}
interface ICallbackToggleFav {
    fun toggleFav(manga: Manga): Boolean
}

interface ICallbackFavInput : ICallbackToggleFav {
    fun onToggleFavResponse(success: Boolean)
}

interface ICallbackDialogPlus {
    var dialogItems: Array<DialogMenusItem>
    var longClickItem: CallbackLongClickItem?
    var menusAdapter: DialogMenuAdapter
    fun initDialogPlus()
}

interface ICallbackDialogItemClicked {
    fun onDialogItemClicked(dataItemPosition: Int, menuPosition: Int, event: Any?)
}

interface ICallbackRv {
    fun initRv()
}

interface ICallbackRealm<V : RealmObject> : ICallbackRv {
    fun deleteAll(f: (() -> Unit)? = null)
    fun onToggle()
    val rvMain: RecyclerView
    val rvFilter: RecyclerView
    var adapterMain: BaseRvRealmAdapter<V>?
    var adapterFilter: BaseRvRealmAdapter<V>?
    var layoutManagerMain: StaggeredGridLayoutManagerWrapper
    var layoutManagerFilter: StaggeredGridLayoutManagerWrapper
    fun adapterClicked(ad: BaseRvRealmAdapter<V>, position: Int, f: (() -> Unit)? = null)
}

interface ICallbackMiniRealm<V : RealmObject> : ICallbackRv {
    fun deleteAll(f: (() -> Unit)? = null)
    fun onToggle()
    fun initBtnEdit()
    val btnEdit: Button
    val rvMain: RecyclerView
    var adapterMain: BaseRvRealmAdapter<V>?
    var layoutManagerMain: RecyclerView.LayoutManager
    fun adapterClicked(ad: BaseRvRealmAdapter<V>, position: Int, f: (() -> Unit)? = null)
}
interface ICallbackBottomFun {
    fun initBottomFun()
    fun undo()
    fun submit()
    fun toggleSelectAll()
    var allSelected: Boolean?
    val bottomFunView: SimpleViewAnimator
    var undoBtn: UndoBtn?
    val btnSelect: Button
    val btnUndo: Button
    val btnSubmit: Button
}

interface IRV<V : Any?> {
    fun clear(): Boolean
    fun removeAll(elements: List<V>?): Boolean
    fun recoverAll(elements: List<V>?): Boolean
    fun addAll(items: List<V>?): Boolean
    fun removeAt(position: Int)
}

interface ISelectable {
    fun toggleSelected(selected: Boolean?, f: (() -> Unit)? = null)
}
interface IRvSelectable<V : Any?> {
    //return all item selected or not
    fun onSelected(position: Int, allSelected: Boolean? = null): Boolean

    fun obtainCountSelected(selected: Boolean)
    //    fun selectedItems(): List<V>?
    fun selectedItems(): List<IndexedValue<V>>?
    var itemsSelectable: Boolean?
    fun onOriginal()
    //not use
    fun addSelectableItem(item: SelectableItem)

    fun addSelectableItems(size: Int)
//    fun removeSelectableItems(indices: List<Int>)
}
interface ICallbackOnClick {
    fun onClick(position: Int, event: Int)
}

interface ICallbackOnLongClick {
    fun onLongClick(position: Int, event: Int)
}

interface ICallbackLoadMore {
    val errorViewLoadMore: ErrorView?
    val subTitleLoadMore: Int?
    var isDataEnd: Boolean
    var page: Int
    //    fun showErrorViewLoadMore(show: Boolean)
    fun onLoadMore()

    fun reset(f: (() -> Unit)? = null)
    fun adapterEmpty(empty: Boolean)
}

interface IAdapterLoadMore {
    fun onAdapterLoadMore(f: (() -> Unit)? = null)
    fun reset(notify: Boolean? = false)
    fun onAdapterLoadMoreFinished(f: (() -> Unit)? = null)
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

interface ICallbackSearchView : IBackListener, ICallbackEmptyDataViewHolder {
    fun onSearch(query: String)
    fun showSearchHistory()
    var query: String?
    var category: Category?

}

interface ICallbackEmptyDataViewHolder : ICallbackDataEmpty {
    val layoutEmptyData: View?
    val tvDesEmptyData: TextView?
    val emptyDesResId: Int
    fun initEmptyDataView()
    fun showEmptyDataView(show: Boolean, emptyResId: Int? = null)
    var emptyDataView: EmptyDataView?
}

interface ICallbackEmptyDataView {
    fun hideSomething()
    fun showEmptyDataView(show: Boolean, emptyResId: Int? = null)
}

interface ICallbackSearch {
    fun submitSearchSuggestion(query: String)
    fun submitSearch()
}

interface ICallbackToolbarSearch : ICallbackSearch {
    fun onSearchHistoryClicked(searchQueryRealm: SearchQueryRealm)
    val mCallbackSearchView: ICallbackSearchView?
}

interface ICallbackFragmentOptionMenu {
    val optionsMenuId: Int
}

interface ICallbackEdit {
    fun initBtnDone()
    fun showBottomFunView(show: Boolean)
    val btnDone: Button
    fun toggleEdit(edit: Boolean)
    fun done()
}

interface ICallbackSpnTracker {
    val spnFilterTracker: AdapterTracker
}

interface ICallbackSort : ICallbackPagerBtns {
    //    fun initSpnFilters()
//    val spnFilter: NiceSpinner
    val mFilters: List<Filter>
    fun sort()
}
interface ICallbackReceiveOptionsMenu {
    fun onReceiveOptionsMenuItem(item: MenuItem)
}

interface ICallbackOptionMenu {
    fun showOptionsMenu(show: Boolean)
    fun showOptionsMenu(optionsMenuId: Int)
}

interface ICallbackLayoutSearch : ICallbackSearch {
    val mLayoutInputText: LinearLayout
    val edtSearch: EditText
    val imgClear: IconTextView
    fun initSearch()
    var searchPassByTime: PassByTime?
    fun initSearchPassByTime()
    fun initImeActionSearch()
    fun initRxSearch()
    fun initImgClear()
}

interface ICallbackLocalSearch : ICallbackLayoutSearch {
    fun search(query: String)
    fun onRealmFilterNull()
    fun restoreOriginalData()
}
interface ICallbackToolbar : ICallbackToolbarSearch, ICallbackOptionMenu, ICallbackLayoutSearch {
    var optionsMenu: Int
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

    fun setScrollToolbarFlag(info: Boolean)
    fun setToolbarTitle(title: String?)
    fun showBtnBack(visi: Boolean)
    fun showBtnBack()
    fun hideDrawerToggle()

    fun showDrawer()
    fun syncStateDrawer()
    fun onSearchScreenRequested()

    fun showConfirmExit()
    fun onAbout()
    fun onSettings()
    fun onSearch(search: Boolean)
    fun onShare()
    fun onFollow()
    fun sendOptionsMenuItem(item: MenuItem)
}

interface IBackListener {
    fun onBackPressed(f: (() -> Unit)? = null)
}

interface ICallbackDataEmpty {
    fun isDataEmpty(): Boolean = false
}

interface ICallbackErrorView : ICallbackDataEmpty {
    val errorView: ErrorView?
    val subTitle: Int?
    fun initErrorView(errorView: ErrorView?, subTitle: Int?)
    fun showErrorView(show: Boolean, f: (() -> Unit)? = null): Boolean
    fun onErrorViewDemand(errorView: ErrorView?)
    fun onErrorViewRetry(errorView: ErrorView?, f: () -> Unit)
    fun loadInterrupted()
}

interface ICallbackSpanCount {
    val spanCount: Int
}

interface IBus {
    fun send(obj: Any?)
    fun asFlowable(): Flowable<Any?>
}

interface ICallbackBus {
    //ibus to receive signal
    fun initIBus()
}
