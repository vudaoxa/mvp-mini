package net.mfilm.ui.mangas

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_mangas.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Category
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.rv.BaseLoadMoreFragment
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.mangas.rv.MangasRvAdapter
import net.mfilm.utils.*
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class MangasFragment : BaseLoadMoreFragment(), MangasMvpView, ICallbackSearchView, IBackListener {
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
        get() = false
        set(value) {}

    @Inject
    lateinit var mMangasPresenter: MangasMvpPresenter<MangasMvpView>
    var mMangasRvAdapter: MangasRvAdapter<Manga>? = null
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_mangas, container, false)
    }

    override fun initViews() {
        initSpnFilters()
        initRv()
        initSwipe()
        if (!search) {
            requestMangas()
        } else {
            //show search history
        }
    }

    override fun initFields() {
        activityComponent.inject(this)
        mMangasPresenter.onAttach(this)
        search = arguments.getBoolean(KEY_SEARCH)
        category = arguments.getSerializable(KEY_CATEGORY) as? Category?
        back = search || category != null
        title = category?.name
    }

    inner class AdapterTracker : AdapterView.OnItemSelectedListener {
        var mPosition = 0
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            mPosition = position
            reset()
            requestMangas()
        }
    }

    fun initRv() {
        rv.apply {
            val spanCount = resources.getInteger(R.integer.mangas_span_count)
            mMangasRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mMangasRvLayoutManagerWrapper
            setupOnLoadMore(this, mCallBackLoadMore)
        }
    }

    val spnFilterTracker = AdapterTracker()
    fun initSpnFilters() {
        val banksAdapter = ArrayAdapter(activity, R.layout.item_spn_filter, filters.map { getString(it.resId) })
        spn_filter.setAdapter(banksAdapter)
        spn_filter.setOnItemSelectedListener(spnFilterTracker)
    }

    //use when refresh(by filter, or reload)
    override fun reset() {
        super.reset()
        mMangasRvAdapter?.reset()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rv.apply {
                val spanCount = resources.getInteger(R.integer.mangas_span_count)
                mMangasRvLayoutManagerWrapper.spanCount = spanCount
                requestLayout()
            }
        })
    }

    override fun initSwipe() {
        swipeContainer.apply {
            if (pullToRefreshEnabled()) {
                setOnRefreshListener { onRefresh() }
                setColorSchemeResources(*pullToRefreshColorResources)
            } else {
                isEnabled = false
            }
        }
    }

    override fun pullToRefreshEnabled() = true
    override val pullToRefreshColorResources: IntArray
        get() = intArrayOf(R.color.colorPrimary, R.color.blue, R.color.green)

    override fun onRefresh() {
        handler({
            reset()
            requestMangas()
            setRefreshing(false)
        }, 2000)
    }

    override fun setRefreshing(refreshing: Boolean) {
        swipeContainer.apply {
            isRefreshing = refreshing
        }
    }

    override fun requestMangas() {
        mMangasPresenter.requestMangas(category?.id, LIMIT, page++, filters[spnFilterTracker.mPosition].content, query)
    }

    override fun onMangasResponse(mangasResponse: MangasResponse?) {
        hideLoading()
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
            onAdapterLoadMoreFinished {
                nullByAdapter(true)
            }
        } ?: let { nullByAdapter(false) }
    }

    //notEmpty condition
    override fun buildMangas(mangas: List<Manga>) {
        Timber.e("---------------buildMangas---------------${mangas.size}")
        spn_filter.show(true)
        mMangasRvAdapter?.apply {
            onAdapterLoadMoreFinished {
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

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        if (event != TYPE_ITEM) return
        mMangasRvAdapter?.mData?.apply {
            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO, typeContent = null, obj = this[position])
        }
    }

    override fun onSearch(query: String) {
        this.query = query
        reset()
        requestMangas()
    }

    override fun onBackPressed() {
        if (!search) return

    }
}