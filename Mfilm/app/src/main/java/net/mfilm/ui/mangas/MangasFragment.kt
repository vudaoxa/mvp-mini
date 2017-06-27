package net.mfilm.ui.mangas

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.fragment_mangas.*
import kotlinx.android.synthetic.main.item_history_clear.*
import net.mfilm.R
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.Category
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.rv.BaseLoadMoreFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_SEARCH_HISTORY
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.rv.MangasRealmRvAdapter
import net.mfilm.ui.mangas.rv.MangasRvAdapter
import net.mfilm.ui.mangas.search.SearchHistoryMvpPresenter
import net.mfilm.utils.*
import timber.log.Timber
import tr.xip.errorview.ErrorView
import java.io.Serializable
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class MangasFragment : BaseLoadMoreFragment(), MangasMvpView, ICallbackSearchView {
    companion object {
        //to assign it to BaseStackActivity
        private var mSearchInstance: MangasFragment? = null
        const val KEY_SEARCH = "KEY_SEARCH"
        const val KEY_CATEGORY = "KEY_CATEGORY"
        fun getSearchInstance(): MangasFragment {
            mSearchInstance?.apply { return this }
            return newInstance(null, true)
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

    override val spnFilterTracker = AdapterTracker({
        mMangasRvAdapter?.reset()
        onErrorViewDemand(errorView)
    })
    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)

    override val layoutEmptyData: View?
        get() = layout_empty_data
    override val tvDesEmptyData: TextView?
        get() = tv_des
    override val emptyDesResId: Int
        get() {
            category?.apply { return R.string.empty_data_category }
            if (searching) return R.string.empty_data_search
            return R.string.empty_data_
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
    override var isDataEnd: Boolean
        get() = !rv.isVisible()
        set(value) {}

    @Inject
    lateinit var mMangasPresenter: MangasMvpPresenter<MangasMvpView>
    @Inject
    lateinit var mSearchHistoryPresenter: SearchHistoryMvpPresenter<MangasMvpView>
    var mMangasRvAdapter: MangasRvAdapter<Manga>? = null
    var mSearchQueryRvAdapter: MangasRealmRvAdapter<SearchQueryRealm>? = null
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_mangas, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMangasPresenter.onDetach()
        mSearchHistoryPresenter.onDetach()
    }

    override fun initFields() {
        activityComponent.inject(this)
        mMangasPresenter.onAttach(this)
        mSearchHistoryPresenter.onAttach(this)
        searching = arguments.getBoolean(KEY_SEARCH)
        category = arguments.getSerializable(KEY_CATEGORY) as? Category?
        back = searching || category != null
        title = category?.name
    }

    override fun initViews() {
        super.initViews()
        initSpnFilters()
        initEmptyDataView()
        initRv()
        initSwipe()
        if (searching) {
            requestSearchHistory()
        } else {
            requestMangas()
        }
    }

    override fun initEmptyDataView() {
        emptyDataView = EmptyDataView(context, spn_filter, layoutEmptyData, tvDesEmptyData, emptyDesResId)
    }
    override fun isDataEmpty(): Boolean {
        mMangasRvAdapter?.apply {
            return itemCount == 0
        }
        return true
    }

    override fun initRv() {
        rv.apply {
            mMangasRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mMangasRvLayoutManagerWrapper
            setupOnLoadMore(this, mCallBackLoadMore)
        }
        rv_search_history.apply {
            layoutManager = LinearLayoutManagerWrapper(context)
        }
    }

    override fun initSpnFilters() {
        val banksAdapter = ArrayAdapter(activity, R.layout.item_spn_filter, filters.map { getString(it.resId) })
        spn_filter.setAdapter(banksAdapter)
        spn_filter.setOnItemSelectedListener(spnFilterTracker)
    }

    override fun requestSearchHistory() {
        mSearchHistoryPresenter.requestSearchHistory()
    }

    override fun onSearchHistoryResponse(searchHistoryRealms: List<SearchQueryRealm>?) {
        hideLoading()
        searchHistoryRealms.let { shr ->
            shr?.apply {
                if (shr.isNotEmpty()) {
                    buildSearchHistory(shr)
                } else onSearchHistoryNull()
            } ?: let { onSearchHistoryNull() }
        }
    }

    override fun onSearchHistoryNull() {
        Timber.e("----------------onSearchHistoryNull------------------")
    }

    override fun buildSearchHistory(searchHistoryRealms: List<SearchQueryRealm>) {
        Timber.e("---------------buildSearchHistory---------------${searchHistoryRealms.size}")
        mSearchQueryRvAdapter?.apply {
            mData?.clear()
            mData?.addAll(searchHistoryRealms)
            notifyDataSetChanged()
        } ?: let {
            mSearchQueryRvAdapter = MangasRealmRvAdapter(context, searchHistoryRealms.toMutableList(), this)
            rv_search_history.adapter = mSearchQueryRvAdapter
        }
    }

    //use when refresh(by filter, or reload)
//    override fun reset() {
//        super.reset()
//        mMangasRvAdapter?.reset()
//    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rv.apply {
                mMangasRvLayoutManagerWrapper.spanCount = spanCount
                requestLayout()
            }
        })
    }

    override fun onBackPressed(f: (() -> Unit)?) {
        if (rv_search_history.isVisible()) {
            f?.invoke()
        } else showSearchHistory()
    }

    override fun showSearchHistory() {
        layout_mangas.show(false)
        errorView?.show(false)
        errorViewLoadMore?.show(false)
        emptyDataView?.showEmptyDataView(false)
        rv_search_history.show(true)

    }
    override fun requestMangas() {
        val position = spnFilterTracker.mPosition
        Timber.e("---------------requestMangas------ $position--------------------")
        mMangasPresenter.requestMangas(category?.id, LIMIT, page, filters[position].content, query)
    }

    override fun onMangasResponse(mangasResponse: MangasResponse?) {
        showErrorView(false)
        mangasResponse.let { mr ->
            mr?.apply {
                mr.mangasPaging.let { mp ->
                    mp?.apply {
                        isDataEnd = TextUtils.isEmpty(nextPageUrl)
                        mp.mangas.let { mgs ->
                            mgs?.apply {
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
        mMangasRvAdapter?.apply {
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
        page++
        spn_filter.show(true)
        emptyDataView?.showEmptyDataView(false)
        mMangasRvAdapter?.apply {
            onAdapterLoadMoreFinished {
                setRefreshed(false, { mMangasRvAdapter?.reset() })
                mData?.addAll(mangas)
                notifyDataSetChanged()
            }
        } ?: let {
            mMangasRvAdapter = MangasRvAdapter(context, mangas.toMutableList(), this)
            rv.adapter = mMangasRvAdapter
        }
    }

    override fun onLoadMore() {
        Timber.e("--------------------onLoadMore----------------------")
        mMangasRvAdapter?.onAdapterLoadMore { requestMangas() }
    }

    override fun adapterEmpty(empty: Boolean) {
        super.adapterEmpty(empty)
        if (empty) {
            emptyDataView?.apply {
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

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                query?.apply {
                    mSearchHistoryPresenter.saveQuery(this)
                }
                mMangasRvAdapter?.mData?.apply {
                    screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO, typeContent = null, obj = this[position])
                }
            }
            TYPE_ITEM_SEARCH_HISTORY -> {
                mSearchQueryRvAdapter?.mData?.apply {
                    baseActivity?.onSearchHistoryClicked(get(position))
                }
            }
        }
    }

    override fun onSearch(query: String) {
        this.query = query
        layout_mangas.show(true)
        rv_search_history.show(false)
        reset { mMangasRvAdapter?.reset(true) }
        requestMangas()
    }
}