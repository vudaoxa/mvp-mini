package net.mfilm.ui.mangas

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_mangas.*
import kotlinx.android.synthetic.main.switch_btns.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Category
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.rv.BaseLoadMoreFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.custom.SwitchButtonItem
import net.mfilm.ui.custom.SwitchButtons
import net.mfilm.ui.dialog_menus.DialogMenuAdapter
import net.mfilm.ui.manga.CallbackLongClickItem
import net.mfilm.ui.manga.DialogMenusItem
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.Filter
import net.mfilm.ui.mangas.rv.MangasRvAdapter
import net.mfilm.ui.search_history.SearchHistoryFragment
import net.mfilm.ui.search_history.SearchHistoryMvpView
import net.mfilm.utils.*
import timber.log.Timber
import tr.xip.errorview.ErrorView
import java.io.Serializable
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class MangasFragment : BaseLoadMoreFragment(), MangasMvpView {
    companion object {
        //to assign it to BaseStackActivity
        private var mSearchInstance: MangasFragment? = null
        const val KEY_SEARCH = "KEY_SEARCH"
        const val KEY_CATEGORY = "KEY_CATEGORY"
        fun getSearchInstance(): MangasFragment {
            return mSearchInstance?.run { this }
                    ?: newInstance(null, true)
        }

        fun newInstance(category: Any? = null, search: Boolean = false): MangasFragment {
            val fragment = MangasFragment()
            val bundle = Bundle()
            bundle.putBoolean(KEY_SEARCH, search)
            bundle.putSerializable(KEY_CATEGORY, category as? Serializable?)
            fragment.arguments = bundle
            if (search) mSearchInstance = fragment
            return fragment
        }
    }

    override val optionsMenuId: Int
        get() = R.menu.main
    private var mPagerPosition = 0
    override var pagerPosition: Int
        get() = mPagerPosition
        set(value) {
            mPagerPosition = value
        }
    override val btns: List<View>
        get() = listOf(btn_hottest, btn_newest, btn_all)
    override val layoutPagerBtns: View
        get() = layout_pager_btns
    private var mPagerBtns: SwitchButtons? = null
    override var pagerBtns: SwitchButtons?
        get() = mPagerBtns
        set(value) {
            mPagerBtns = value
        }
    override val mSearchHistoryView: SearchHistoryMvpView
        get() = SearchHistoryFragment.getInstance()
    override val searchHistoryContainerId: Int
        get() = R.id.container_search_history
    override val searchHistoryContainerView: View
        get() = container_search_history
    private var mDialogItems = arrayOf<DialogMenusItem>()
    override var dialogItems: Array<DialogMenusItem>
        get() = mDialogItems
        set(value) {
            mDialogItems = value
        }
    private var mMenusAdapter: DialogMenuAdapter? = null
    override var menusAdapter: DialogMenuAdapter
        get() = mMenusAdapter!!
        set(value) {
            mMenusAdapter = value
        }
    private var mLongClickItem: CallbackLongClickItem? = null
    override var longClickItem: CallbackLongClickItem?
        get() = mLongClickItem
        set(value) {
            mLongClickItem = value
        }
    override val mFilters: List<Filter>
        get() = filters

    override fun sort() {
        mMangasRvAdapter?.reset()
        onErrorViewDemand(errorView)
    }

    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)

    override val layoutEmptyData: View?
        get() = layout_empty_data
    override val tvDesEmptyData: TextView?
        get() = tv_des
    override val emptyDesResId: Int
        get() {
            return category?.run { R.string.empty_data_category } ?: let {
                if (searching) R.string.empty_data_search
                else R.string.empty_data_
            }
        }
    private var mEmptyDataView: EmptyDataView? = null
    override var emptyDataView: EmptyDataView?
        get() = mEmptyDataView
        set(value) {
            mEmptyDataView = value
        }
    override val swipeContainer: SwipeRefreshLayout?
        get() = swipe

    override val errorView: ErrorView?
        get() = error_view
    override val subTitle: Int?
        get() = R.string.failed_to_load
    override val errorViewLoadMore: ErrorView?
        get() = error_view_load_more as? ErrorView?
    override val subTitleLoadMore: Int?
        get() = R.string.failed_to_load_more

    override fun onErrorViewDemand(errorView: ErrorView?) {
        if (mSearchHistoryView.isHistoryVisible()) return
        when (errorView) {
            this.errorView -> {
                reset()
                requestMangas()
            }
            errorViewLoadMore -> {
                onLoadMore()
            }
        }
    }

    private var mQuery: String? = null
    override var query: String?
        get() = mQuery
        set(value) {
            mQuery = value
        }
    private var mCategory: Category? = null
    override var category: Category?
        get() = mCategory
        set(value) {
            mCategory = value
        }
    private var dataEnd = false
    override var isDataEnd: Boolean
        get() = dataEnd
        set(value) {
            dataEnd = value
        }

    @Inject
    lateinit var mMangasPresenter: MangasMvpPresenter<MangasMvpView>

    var mMangasRvAdapter: MangasRvAdapter<Manga>? = null

    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_mangas, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMangasPresenter.onDetach()
    }

    override fun initFields() {
        tryOrExit {
            activityComponent?.inject(this)
            mMangasPresenter.onAttach(this)
        }
        searching = arguments.getBoolean(KEY_SEARCH)
        category = arguments.getSerializable(KEY_CATEGORY) as? Category?
        back = searching || category != null
        title = category?.name ?: getString(R.string.manga)
        searchable = true
    }

    override fun initViews() {
        super.initViews()
        initRv()
        initPagerBtns()
        initEmptyDataView()
        initSwipe()
        initDialogPlus()
        initAds()
        if (searching) {
            attachSearchHistoryFragment()
        } else {
            requestMangas()
        }
    }

    override fun initDialogPlus() {
        val dialogItemsTitle = context.resources.getStringArray(R.array.bottom_dialog)
        dialogItems = arrayOf(DialogMenusItem(dialogItemsTitle[0], DialogMenus.SHARE),
                DialogMenusItem(dialogItemsTitle[1], DialogMenus.FAVORITES))
        menusAdapter = DialogMenuAdapter(context, R.layout.item_dialog_menu, dialogItems)
        longClickItem = CallbackLongClickItem({ x: Int, y: Int, z: Any? -> onDialogItemClicked(x, y, z) })
    }

    override fun onDialogItemClicked(dataItemPosition: Int, menuPosition: Int, event: Any?) {
        Timber.e("-------onDialogItemClicked----------$dataItemPosition-----------$menuPosition-------$event---")
        mMangasRvAdapter?.mData?.run {
            val manga = this[dataItemPosition]
            when (event) {
                DialogMenus.FAVORITES -> {
                    toggleFav(manga)
                }
                DialogMenus.SHARE -> {

                }
                else -> {
                }
            }
        }
    }

    override fun initPagerBtns() {
        var i = 0
        val btnItems = btns.zip(mFilters, { b, f -> SwitchButtonItem(i++, b, f, true) })
        pagerBtns = SwitchButtons(btnItems, { j -> onPagerSelected(j) })
    }

    override fun onPagerSelected(i: Int) {
        Timber.e("currentItem----------------$i")
        if (pagerPosition == i) return
        pagerPosition = i
        sort()
    }

    override fun initEmptyDataView() {
        emptyDataView = EmptyDataView(context, layoutPagerBtns, layoutEmptyData, tvDesEmptyData, emptyDesResId)
    }

    override fun showEmptyDataView(show: Boolean, emptyResId: Int?) {
        emptyDataView?.showEmptyDataView(show)
    }

    override fun isDataEmpty(): Boolean {
        return mMangasRvAdapter?.run {
            itemCount == 0
        } ?: true
    }

    override fun initRv() {
        rv.run {
            mMangasRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mMangasRvLayoutManagerWrapper
            setupOnLoadMore(this, mCallBackLoadMore)
        }
    }

    override fun toggleFav(manga: Manga): Boolean {
        return mMangasPresenter.toggleFav(manga)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rv.run {
                mMangasRvLayoutManagerWrapper.spanCount = spanCount
                requestLayout()
            }
        })
    }

    override fun onBackPressed(f: (() -> Unit)?) {
        if (mSearchHistoryView.isHistoryVisible()) {
            f?.invoke()
        } else showSearchHistory()
    }

    override fun showSearchHistory() {
        layout_mangas.show(false)
        errorView?.show(false)
        errorViewLoadMore?.show(false)
        emptyDataView?.showEmptyDataView(false)
        mSearchHistoryView.show(true)

    }

    override fun attachSearchHistoryFragment() {
        attachChildFragment(searchHistoryContainerView, searchHistoryContainerId, SearchHistoryFragment.newInstance())
    }

    override fun requestMangas() {
        Timber.e("---------------requestMangas------ $pagerPosition--------------------")
        mMangasPresenter.requestMangas(category?.id, LIMIT, page, mFilters[pagerPosition].content, query)
    }

    override fun onMangasResponse(mangasResponse: MangasResponse?) {
        Timber.e("---onMangasResponse---------${mangasResponse?.mangasPaging?.mangas?.size}------------------------------------")
        showErrorView(false)
        mangasResponse.let { mr ->
            mr?.run {
                mr.mangasPaging.let { mp ->
                    mp?.run {
                        isDataEnd = TextUtils.isEmpty(nextPageUrl)
                        mp.mangas.let { mgs ->
                            mgs?.run {
                                if (mgs.isNotEmpty()) {
                                    buildMangas(mgs)
                                } else onMangasNull()
                            } ?: let { onMangasNull() }
                        }
                    } ?: let { onMangasNull() }
                }
            } ?: let { onMangasNull() }
        }
    }

    override fun onMangasNull() {
        Timber.e("----------------onMangasNull-----------------")
        mMangasRvAdapter?.run {
            if (itemCount == 0) {
                adapterEmpty(true)
            } else {
                onAdapterLoadMoreFinished {
                    adapterEmpty(false)
                }
            }
        } ?: let { adapterEmpty(true) }
    }

    //notEmpty condition
    override fun buildMangas(mangas: List<Manga>) {
        Timber.e("---------------buildMangas---------------${mangas.size}")
        layoutPagerBtns.show(true)
        emptyDataView?.showEmptyDataView(false)
        mMangasRvAdapter?.run {
            onAdapterLoadMoreFinished {
                setRefreshed(false, { mMangasRvAdapter?.reset() })
                mData?.addAll(mangas)
                notifyDataSetChanged()
            }
        } ?: let {
            mMangasRvAdapter = MangasRvAdapter(context, mangas.toMutableList(), this, this)
            rv.adapter = mMangasRvAdapter
        }
        interAds(page++)
    }

    override fun onLoadMore() {
        Timber.e("--------------------onLoadMore----------------------")
        mMangasRvAdapter?.onAdapterLoadMore { requestMangas() }
    }

    override fun adapterEmpty(empty: Boolean) {
        super.adapterEmpty(empty)
        if (empty) {
            emptyDataView?.run {
                hideSomething()
                showEmptyDataView(true)
            }
        }
    }

    override fun loadInterrupted() {
        super.loadInterrupted()
        mMangasRvAdapter?.onAdapterLoadMoreFinished()
        emptyDataView?.hideSomething()
    }

    override fun onToggleFavResponse(success: Boolean) {
        when (success) {
            true -> {
                showMessage(AppConstants.TYPE_TOAST_SUCCESS, R.string.fav_success)
            }
            else -> {
                showMessage(AppConstants.TYPE_TOAST_ERROR, R.string.fav_failed)
            }
        }
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        capture(position, event)
        mMangasRvAdapter?.mData?.run {
            val manga = this[mPosition]
            manga.onClicked {
                interAds(null, { action() })
            }
        }
    }

    fun action() {
        when (mEvent) {
            TYPE_ITEM -> {
                query?.run {
                    mSearchHistoryView.saveQuery(this)
                }
                mMangasRvAdapter?.mData?.run {
                    val manga = this[mPosition]
                    manga.onClicked {
                        screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO,
                                typeContent = null, obj = manga)
                    }
                }
            }
        }
    }

    override fun onLongClick(position: Int, event: Int) {
        Timber.e("---------------------onLongClick--------------------$position")
        mMangasRvAdapter?.mData?.run {
            val manga = this[position]
            manga.onLongClicked(true, {
                DialogUtils.showBottomDialog(context, menusAdapter, position, longClickItem)
            })
        }
    }

    override fun onSearch(query: String) {
        this.query = query
        layout_mangas.show(true)
        mSearchHistoryView.show(false)
        reset { mMangasRvAdapter?.reset(true) }
        requestMangas()
    }
}