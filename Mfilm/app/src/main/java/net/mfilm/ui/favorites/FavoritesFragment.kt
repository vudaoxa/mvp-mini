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
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import com.joanzapata.iconify.widget.IconTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.bottom_fun_view.*
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.layout_input_text.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_FILTER
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.custom.SimpleViewAnimator
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.Filter
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
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

    private var mAllSelected: Boolean? = null
    override var allSelected: Boolean?
        get() = mAllSelected
        set(value) {
            mAllSelected = value
            var text = R.string.select_all
            if (mAllSelected == true)
                text = R.string.deselect_all
            btnSelect.setText(text)
            mAllSelected?.apply {
                bottomFunView.show(true)
                btnDone.show(true)
                edtSearch.enable(false)
                imgClear.enable(false)
                if (rv.isVisible()) {
                    mMangasRvAdapter?.apply {
                        Timber.e("-----countSelected---- $countSelected--------------------------")
                        btnSubmit.enable(countSelected > 0)
                    }
                } else if (rv_filter.isVisible()) {
                    mMangasFilterRvAdapter?.apply {
                        Timber.e("----filter-countSelected---- $countSelected--------------------------")
                        btnSubmit.enable(countSelected > 0)
                    }
                }
            } ?: let {
                if (rv.isVisible()) {
                    mMangasRvAdapter?.apply {
                        itemsSelectable = null
                        notifyDataSetChanged()
                    }
                } else if (rv_filter.isVisible()) {
                    mMangasFilterRvAdapter?.apply {
                        itemsSelectable = null
                        notifyDataSetChanged()
                    }
                } else {

                }
            }
        }
    override val bottomFunView: SimpleViewAnimator
        get() = bottom_fun
    override val btnSelect: Button
        get() = btn_toggle_select
    override val btnUndo: Button
        get() = btn_undo
    override val btnSubmit: Button
        get() = btn_delete
    override val btnDone: Button
        get() = btn_done
    override val mFilters: List<Filter>
        get() = filtersFavorites
    override val spnFilter: NiceSpinner
        get() = spn_filter
    override val mLayoutInputText: LinearLayout
        get() = layout_input_text
    override val edtSearch: EditText
        get() = edt_search
    override val imgClear: IconTextView
        get() = img_clear

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
        sort()
    })

    @Inject
    lateinit var mFavoritesPresenter: FavoritesMvpPresenter<FavoritesMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    lateinit var mMangasRvFilterLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: BaseRvRealmAdapter<MangaFavoriteRealm>? = null
    var mMangasFilterRvAdapter: BaseRvRealmAdapter<MangaFavoriteRealm>? = null
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
        initBottomFun()
        initBtnDone()
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
                            restoreOriginalData()
                        }
                        imgClear.show(clearShowed)
                    }
                }
        imgClear.setOnClickListener {
            edtSearch.text = null
            restoreOriginalData()
        }
    }

    override fun restoreOriginalData() {
        if (isDataEmpty() || rv.isVisible()) return
        Timber.e("------------------restoreOriginalData--------------------------")
        handler({
            rv.show(true)
            rv_filter.show(false)
            showEmptyDataView(false)
        })
    }

    override fun initEmptyDataView() {
        emptyDataView = EmptyDataView(context, spn_filter, layoutEmptyData, tvDesEmptyData, emptyDesResId)
    }

    override fun initSpnFilters() {
        val banksAdapter = ArrayAdapter(activity,
                R.layout.item_spn_filter, mFilters.map { getString(it.resId) })
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
        rv_filter.apply {
            mMangasRvFilterLayoutManagerWrapper = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = mMangasRvFilterLayoutManagerWrapper
        }
    }

    override fun initBottomFun() {
        btnSelect.setOnClickListener { toggleSelectAll() }
        btnUndo.setOnClickListener { undo() }
        btnSubmit.setOnClickListener { submit() }
    }

    override fun initBtnDone() {
        btnDone.setOnClickListener { done() }
    }

    override fun done() {
        allSelected = null
        edtSearch.enable(true)
        imgClear.enable(true)
        btnDone.show(false)
        bottomFunView.show(false)
    }

    override fun toggleSelectAll() {
        Timber.e("-----toggleSelectAll------allSelected------$allSelected------------------------")
        if (rv.isVisible()) {
            mMangasRvAdapter?.apply {
                allSelected = onSelected(-1, !allSelected!!)
            }
        } else if (rv_filter.isVisible()) {
            mMangasFilterRvAdapter?.apply {
                allSelected = onSelected(-1, !allSelected!!)
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rv.apply {
                mMangasRvLayoutManagerWrapper.spanCount = spanCount
                requestLayout()
            }
            rv_filter.apply {
                mMangasRvFilterLayoutManagerWrapper.spanCount = spanCount
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
                if (!rv.isVisible()) return
                mMangasRvAdapter?.apply {
                    if (itemCount < 2) return
                    mLayoutInputText.show(false)
                    spnFilter.show(true)
                    sort()
                    toggleEdit(false)
                }
            }
            R.id.action_favorites_edit -> {
                if (allSelected != null) return
                mLayoutInputText.show(false)
                spnFilter.show(false)
                toggleEdit(true)
            }
        }
    }

    override fun submitSearch() {
        val query = edtSearch.text.toString().trim()
        if (query.isEmpty()) return
        search(query)
        hideKeyboard()
    }

    override fun submitSearchSuggestion(query: String) {
        search(query)
    }

    override fun search(query: String) {
        val favoritesFilter = mMangasRvAdapter?.mData?.filter { it.name!!.contains(query, true) }
        favoritesFilter.let { ff ->
            ff?.apply {
                if (ff.isNotEmpty()) {
                    buildMangaFavoritesRealmFilter(ff)
                } else onRealmFilterNull()
            } ?: let { onRealmFilterNull() }
        }
    }

    override fun buildMangaFavoritesRealmFilter(objs: List<MangaFavoriteRealm>) {
        Timber.e("----------buildMangaFavoritesRealmFilter-------- ${objs.size}----------------------------")
        mMangasFilterRvAdapter?.apply {
            clear()
            addAll(objs)
            notifyDataSetChanged()
        } ?: let {
            mMangasFilterRvAdapter = BaseRvRealmAdapter(context, objs.toMutableList(), this@FavoritesFragment, this, true)
            rv_filter.adapter = mMangasFilterRvAdapter
        }
        showEmptyDataView(false)
        handler({
            rv.show(false)
            rv_filter.show(true)
        })
    }

    override fun onRealmFilterNull() {
        Timber.e("-------------onRealmFilterNull-----------------------------")
        rv.show(false)
        rv_filter.show(false)
        showEmptyDataView(true)
    }

    override fun sort() {
        Timber.e("------------sort---------------------------")
        val filter = mFilters[spnFilterTracker.mPosition]
        when (filter.content) {
            TYPE_FILTER_AZ -> {
                mMangasRvAdapter?.apply {
                    mData?.sortBy { it.name }
                    notifyDataSetChanged()
                }
            }
            TYPE_FILTER_TIME -> {
                mMangasRvAdapter?.apply {
                    mData?.sortByDescending { it.time }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun toggleEdit(edit: Boolean) {
        if (!edit)
            done()
        else {
            if (rv.isVisible()) {
                mMangasRvAdapter?.apply {
                    allSelected = onSelected(-1)
                }
            } else if (rv_filter.isVisible()) {
                mMangasFilterRvAdapter?.apply {
                    allSelected = onSelected(-1)
                }
            }
        }
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
        mMangasRvAdapter.let { ad ->
            ad?.apply {
                allSelected?.apply {
                    if (undo) {
                        //btnUndo clicked
                        val x = ad.recoverAll(selectedItems)
                        Timber.e("----------recoverAll----------------$x-------${ad.itemCount}-----------")
                        ad.notifyDataSetChanged()
                        btnUndo.show(false)
                        undo = false
                    } else {
                        //after delete selected items
                        val x = ad.removeAll(selectedItems)
                        Timber.e("----------removeAll----------------$x------------------")
                        ad.notifyDataSetChanged()
                        btnUndo.show(true)
                    }

                } ?: let {
                    ad.clear()
                    ad.notifyDataSetChanged()
                    setScrollToolbarFlag(true)
                    showEmptyDataView(true)
                }
            }
        }

    }

    /*override fun onFavoritesNull() {
        Timber.e("----------------onFavoritesNull------------------")
        mMangasRvAdapter?.apply {
            clear()
            notifyDataSetChanged()
        }
        setScrollToolbarFlag(true)
        showEmptyDataView(true)
        allSelected?.apply {
            btnUndo.show(!undo)
        }
    }
*/
    override fun showEmptyDataView(show: Boolean) {
        emptyDataView?.showEmptyDataView(show)
    }

    //it's for rv only
    override fun buildFavorites(mangaFavoriteRealms: List<MangaFavoriteRealm>) {
        Timber.e("---------------buildFavorites--------$context-------${mangaFavoriteRealms.size}")
        context ?: return
        showEmptyDataView(false)
        setScrollToolbarFlag(false)
        mMangasRvAdapter.let { ad ->
            ad?.apply {
                allSelected?.apply {
                    if (undo) {
                        //btnUndo clicked
                        val x = ad.recoverAll(selectedItems)
                        Timber.e("----------recoverAll----------------$x-------${ad.itemCount}-----------")
                        ad.notifyDataSetChanged()
                        btnUndo.show(false)
                        undo = false
                    } else {
                        //after delete selected items
                        val x = ad.removeAll(selectedItems)
                        Timber.e("----------removeAll----------------$x------------------")
                        ad.notifyDataSetChanged()
                        btnUndo.show(true)
                    }

                } ?: let {
                    //original state, no selection
                    Timber.e("---------------buildFavorites--------clear------")
                    ad.clear()
                    ad.addAll(mangaFavoriteRealms)
                    ad.notifyDataSetChanged()
                }
            } ?: let {
                //the first build
                mMangasRvAdapter = BaseRvRealmAdapter(context, mangaFavoriteRealms.toMutableList(), this, this)
                rv.adapter = mMangasRvAdapter
            }
        }
    }

    private var undo = false
    override fun undo() {
        selectedItems?.apply {
            undo = true
            mFavoritesPresenter.toggleFav(this)
        }
    }

    private var selectedItems: List<MangaFavoriteRealm>? = null
    override fun submit() {
        val adapter = if (rv.isVisible()) {
            mMangasRvAdapter
        } else if (rv_filter.isVisible()) {
            mMangasFilterRvAdapter
        } else null

        fun updateBtnSubmit() {
            adapter?.apply {
                btnSubmit.enable(countSelected > 0)
            }
        }
        selectedItems = adapter?.selectedItems()?.map { it.value }
        selectedItems?.apply {
            fun doIt() {
                mFavoritesPresenter.toggleFav(this)
                btnUndo.show(true)
            }
            Timber.e("------selectedItems--------$indices---------------------")
            if (isNotEmpty()) {
                DialogUtil.showMessageConfirm(context, R.string.notifications, R.string.confirm_delete,
                        MaterialDialog.SingleButtonCallback { _, _ -> doIt() })
            } else {
                updateBtnSubmit()
            }
        } ?: let {
            updateBtnSubmit()
        }
    }

    override fun isDataEmpty(): Boolean {
        mMangasRvAdapter?.apply {
            return itemCount == 0
        }
        return true
    }

//    override fun obtainSelect(allSelected: Boolean) {
//
//    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                mMangasRvAdapter.let { ad ->
                    ad?.apply {
                        ad.itemsSelectable?.apply {
                            allSelected = ad.onSelected(position)
                        } ?: let {
                            ad.mData?.apply {
                                screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO,
                                        typeContent = null, obj = this[position].id)
                            }
                        }
                    }
                }
            }
            TYPE_ITEM_FILTER -> {
                mMangasFilterRvAdapter.let { ad ->
                    ad?.apply {
                        ad.itemsSelectable?.apply {
                            allSelected = ad.onSelected(position)
                        } ?: let {
                            ad.mData?.apply {
                                screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO,
                                        typeContent = null, obj = this[position].id)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onLongClick(position: Int, event: Int) {
        Timber.e("---------------------onLongClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                mMangasRvAdapter?.apply {
                    allSelected = onSelected(position)
                }
            }
            TYPE_ITEM_FILTER -> {
                mMangasFilterRvAdapter?.apply {
                    allSelected = onSelected(position)
                }
            }
        }
    }
}