package net.mfilm.ui.favorites

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.jakewharton.rxbinding2.widget.RxTextView
import com.joanzapata.iconify.widget.IconTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.rv.MangasRealmRvAdapter
import net.mfilm.utils.*
import org.angmarch.views.NiceSpinner
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by MRVU on 6/20/2017.
 */
class FavoritesFragment : BaseStackFragment(), FavoritesMvpView {
    companion object {
        fun newInstance(): FavoritesFragment {
            val fragment = FavoritesFragment()
            return fragment
        }
    }

    override val spnFilter: NiceSpinner
        get() = spn_filter
    override val mLayoutInputText: LinearLayout
        get() = layout_input_text
    override val edtSearch: EditText
        get() = edt_search
    override val imgClear: IconTextView
        get() = img_clear
    override val btnDone: Button
        get() = btn_done

    override val optionsMenuId: Int
        get() = R.menu.favorites
    override val layoutEmptyData: View?
        get() = layout_empty_data
    override val tvDesEmptyData: TextView?
        get() = tv_des
    override val emptyDesResId: Int
        get() {
            return R.string.empty_data_favorite
        }
    private var mEmptyDataView: EmptyDataView? = null
    override var emptyDataView: EmptyDataView?
        get() = mEmptyDataView
        set(value) {
            mEmptyDataView = value
        }
    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)
    override val spnFilterTracker = AdapterTracker({
        Timber.e("--------------spnFilterTracker---------")
    })

    @Inject
    lateinit var mFavoritesPresenter: FavoritesMvpPresenter<FavoritesMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: MangasRealmRvAdapter<MangaFavoriteRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFavoritesPresenter.onDetach()
    }
    override fun initFields() {
        searchable = true
        activityComponent.inject(this)
        mFavoritesPresenter.onAttach(this)
        title = getString(R.string.favorites)
    }

    override fun initViews() {
        initSearch()
        initSpnFilters()
        initEmptyDataView()
        initRv()
        requestFavorites()
    }

    private var searchTime = -1L
    override fun initSearch() {
        edtSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch()
                //searchTime to avoid conflict between searching and searching suggestion
                searchTime = System.currentTimeMillis()
                Timber.e("time -------------- " + searchTime)
            }
            false
        }
        RxTextView.afterTextChangeEvents(edtSearch)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->
                    var ok = true
                    if (searchTime != -1L) {
                        val currentTime = System.currentTimeMillis()
                        val l = currentTime - searchTime
                        Timber.e(currentTime.toString() + " -------------  " + searchTime + " === " + l)
                        if (l <= AUTO_LOAD_DURATION) {
                            ok = false
                        }
                    }
                    Timber.e("ok----------------- " + ok)
                    if (ok) {
                        val s = tvChangeEvent.view().text.toString()
                        val text = s.trim { it <= ' ' }
                        var clearShowed = true
                        if (text.isNotEmpty()) {
                            submitSearchSuggestion(text)
                        } else {
                            clearShowed = false
                        }
                        imgClear.show(clearShowed)
                    }
                }
        imgClear.setOnClickListener {
            edtSearch.text = null
            restoreFullData()
        }
    }

    fun restoreFullData() {

    }
    override fun initEmptyDataView() {
        emptyDataView = EmptyDataView(context, spn_filter, layoutEmptyData, tvDesEmptyData, emptyDesResId)
    }
    override fun initSpnFilters() {
        val banksAdapter = ArrayAdapter(activity,
                R.layout.item_spn_filter, filtersFavorites.map { getString(it.resId) })
        spn_filter.apply {
            setAdapter(banksAdapter)
            setOnItemSelectedListener(spnFilterTracker)
        }
    }

    override fun initRv() {
        rv.apply {
            mMangasRvLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mMangasRvLayoutManagerWrapper
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rv.apply {
                mMangasRvLayoutManagerWrapper.spanCount = spanCount
                requestLayout()
            }
        })
    }

    override fun onReceiveOptionsMenuItem(item: MenuItem) {
        Timber.e("-----onReceiveOptionsMenuItem-----$isVisible---- ${item.title}--------------------------------------------")
        if (!isVisible || isDataEmpty()) return
        when (item.itemId) {
            R.id.action_favorites_search -> {
                mLayoutInputText.show(true)
                edtSearch.requestFocus()
                spnFilter.show(false)
                toggleEdit(false)
            }
            R.id.action_favorites_sort -> {
                mLayoutInputText.show(false)
                spnFilter.show(true)
                toggleEdit(false)
            }
            R.id.action_favorites_edit -> {
                mLayoutInputText.show(false)
                spnFilter.show(false)
                toggleEdit(true)
            }
        }
    }

    override fun submitSearch() {
        val query = edtSearch.text.toString().trim()
        if (query.isEmpty()) return
//        mFavoritesPresenter.search
        hideKeyboard()
    }

    override fun submitSearchSuggestion(query: String) {

    }

    fun search(query: String) {
        mMangasRvAdapter?.apply {
            val x = mData?.filter { it.name!!.contains(query) }
            xx
        }
    }

    override fun toggleEdit(edit: Boolean) {

    }
    override fun requestFavorites() {
        mFavoritesPresenter.requestFavorites()
    }

    override fun onFavoritesResponse(mangaFavoriteRealms: List<MangaFavoriteRealm>?) {
        hideLoading()
        mangaFavoriteRealms.let { mr ->
            mr?.apply {
                if (mr.isNotEmpty()) {
                    buildFavorites(mr)
                } else onFavoritesNull()
            } ?: let { onFavoritesNull() }
        }
    }

    override fun onFavoritesNull() {
        Timber.e("----------------onFavoritesNull------------------")
        mMangasRvAdapter?.clear()
        emptyDataView?.apply {
            hideSomething()
            showEmptyDataView(true)
        }
    }

    override fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>) {
        Timber.e("---------------buildFavorites---------------${mangaFavoriteRealms.size}")
        context ?: return
        spn_filter.show(true)
        emptyDataView?.showEmptyDataView(false)
        mMangasRvAdapter?.apply {
            clear()
            mData?.addAll(mangaFavoriteRealms)
            notifyDataSetChanged()
        } ?: let {
            mMangasRvAdapter = MangasRealmRvAdapter(context, mangaFavoriteRealms.toMutableList(), this)
            rv.adapter = mMangasRvAdapter
        }
    }

    override fun isDataEmpty(): Boolean {
        mMangasRvAdapter?.apply {
            return itemCount == 0
        }
        return true
    }
    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        if (event != TYPE_ITEM) return
        mMangasRvAdapter?.mData?.apply {
            screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO, typeContent = null, obj = this[position].id)
        }
    }
}