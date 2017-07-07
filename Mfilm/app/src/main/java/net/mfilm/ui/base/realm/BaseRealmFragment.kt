package net.mfilm.ui.base.realm

import android.content.res.Configuration
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import com.afollestad.materialdialogs.MaterialDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.RealmObject
import net.mfilm.R
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_FILTER
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.PassByTime
import net.mfilm.ui.manga.UndoBtn
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
import net.mfilm.utils.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created by MRVU on 7/3/2017.
 */
abstract class BaseRealmFragment<V : RealmObject> : BaseStackFragment(), RealmMvpView<V> {
    private var mAllSelected: Boolean? = null
    override var allSelected: Boolean?
        get() = mAllSelected
        set(value) {
            mAllSelected = value
            var text = R.string.select_all
            if (mAllSelected == true)
                text = R.string.deselect_all
            btnSelect.setText(text)
            mAllSelected?.run {
                //start selected
                showBottomFunView(true)
                spnFilter.show(false)
                if (rvMain.isVisible()) {
                    updateBtnSubmit(adapterMain)
                } else if (rvFilter.isVisible()) {
                    updateBtnSubmit(adapterFilter)
                }
            } ?: let {
                //no selection
                if (rvMain.isVisible()) {
                    adapterMain?.onOriginal()
                } else if (rvFilter.isVisible()) {
                    adapterFilter?.onOriginal()
                } else {

                }
            }
        }

    override fun initViews() {
        initRv()
        initSearch()
        initSpnFilters()
        initEmptyDataView()
        initBottomFun()
        initBtnDone()
    }

    override fun initRv() {
        rvMain.run {
            layoutManagerMain = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = layoutManagerMain
        }
        rvFilter.run {
            layoutManagerFilter = StaggeredGridLayoutManagerWrapper(spanCount,
                    StaggeredGridLayoutManager.VERTICAL)
            layoutManager = layoutManagerFilter
        }
    }

    fun updateBtnSubmit(adapter: BaseRvRealmAdapter<V>?) {
        adapter?.run {
            btnSubmit.enable(countSelected > 0)
        }
    }

    fun onOriginal(ad: BaseRvRealmAdapter<V>, mangaFavoriteRealms: List<V>? = null) {
        ad.clear()
        val x = mangaFavoriteRealms == null
        Timber.e("-----onOriginal-------------$x-----------------")
        showEmptyDataView(x)
        setScrollToolbarFlag(x)
        mangaFavoriteRealms?.run {
            ad.addAll(this)
        }
        ad.notifyDataSetChanged()
    }

    protected var selectedItems: List<V>? = null
    fun doByAllSelected(ad: BaseRvRealmAdapter<V>, mangaFavoriteRealms: List<V>? = null) {
        allSelected?.run {
            if (!isVisible) return
            val x = mangaFavoriteRealms == null
            showEmptyDataView(x)
            setScrollToolbarFlag(x)
            undoBtn?.onSelected(
                    {
                        val y = ad.recoverAll(selectedItems)
                        Timber.e("----------recoverAll----------------$y-------${ad.itemCount}-----------")
                        ad.notifyDataSetChanged()
                        btnSubmit.enable(ad.countSelected > 0)
                    },
                    {
                        val z = ad.removeAll(selectedItems)
                        Timber.e("----------removeAll----------------$z------------------")
                        ad.notifyDataSetChanged()
                        btnSubmit.enable(ad.countSelected > 0)
                    },
                    {
                        deleteAll()
                    }
            )
        } ?: let {
            onOriginal(ad, mangaFavoriteRealms)
        }
    }

    override fun deleteAll(f: (() -> Unit)?) {
        Timber.e("---deleteAll-------allSelected--------- $allSelected-----------------")
        if (allSelected == true && isDataEmpty()) {
            f?.invoke()
        }
    }

    override fun initSearch() {
        initSearchPassByTime()
        initImeActionSearch()
        initRxSearch()
        initImgClear()
    }

    override fun initEmptyDataView() {
        emptyDataView = EmptyDataView(context, spnFilter, layoutEmptyData, tvDesEmptyData, emptyDesResId)
    }

    override fun initBtnDone() {
        btnDone.setOnClickListener { done() }
    }

    override fun initSpnFilters() {
        val banksAdapter = ArrayAdapter(activity,
                R.layout.item_spn_filter, mFilters.map { getString(it.resId) })
        spnFilter.run {
            setAdapter(banksAdapter)
            setOnItemSelectedListener(spnFilterTracker)
        }
    }

    override fun done() {
        allSelected = null
        undoBtn?.reset({ deleteAll() })
        showBottomFunView(false)
    }

    override fun toggleSelectAll() {
        Timber.e("-----toggleSelectAll------allSelected------$allSelected------------------------")
        if (rvMain.isVisible()) {
            adapterMain?.run {
                allSelected = onSelected(-1, !allSelected!!)
            }
        } else if (rvFilter.isVisible()) {
            adapterFilter?.run {
                allSelected = onSelected(-1, !allSelected!!)
            }
        }
    }

    override fun initBottomFun() {
        btnSelect.setOnClickListener { toggleSelectAll() }
        btnSubmit.setOnClickListener { submit() }
        undoBtn = UndoBtn(null, btnUndo, { undo() })
    }

    override fun initSearchPassByTime() {
        searchPassByTime = PassByTime(AUTO_LOAD_DURATION)
    }

    override fun initImeActionSearch() {
        edtSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch()
                //searchTime to avoid conflict between searching and searching suggestion
                searchPassByTime?.run {
                    time = System.currentTimeMillis()
                }
            }
            false
        }
    }

    override fun initRxSearch() {
        RxTextView.afterTextChangeEvents(edtSearch)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->
                    searchPassByTime?.passByTime {
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
    }

    override fun initImgClear() {
        imgClear.setOnClickListener {
            edtSearch.text = null
            restoreOriginalData()
        }
    }

    override fun restoreOriginalData() {
        if (isDataEmpty() || rvMain.isVisible()) return
        Timber.e("------------------restoreOriginalData--------------------------")
        handler({
            rvMain.show(true)
            rvFilter.show(false)
            showEmptyDataView(false)
        })
    }

    override fun showBottomFunView(show: Boolean) {
        edtSearch.enable(!show)
        imgClear.enable(!show)
        btnDone.show(show)
        bottomFunView.show(show)
    }

    override fun toggleEdit(edit: Boolean) {
        if (!edit)
            done()
        else {
            if (rvMain.isVisible()) {
                adapterMain?.run {
                    allSelected = onSelected(-1)
                }
            } else if (rvFilter.isVisible()) {
                adapterFilter?.run {
                    allSelected = onSelected(-1)
                }
            }
        }
    }

    override fun onRealmFilterNull() {
        Timber.e("-------------onRealmFilterNull-----------------------------")
        rvMain.show(false)
        rvFilter.show(false)
        showEmptyDataView(true)
    }

    override fun showEmptyDataView(show: Boolean) {
        emptyDataView?.showEmptyDataView(show)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        handler({
            rvMain.run {
                layoutManagerMain.spanCount = spanCount
                requestLayout()
            }
            rvFilter.run {
                layoutManagerFilter.spanCount = spanCount
                requestLayout()
            }
        })
    }

    override fun onReceiveOptionsMenuItem(item: MenuItem) {
        Timber.e("-----onReceiveOptionsMenuItem-----$isVisible---- ${item.title}----------------")
        if (!isVisible || isDataEmpty()) return
        when (item.itemId) {
            actionSearch -> {
                mLayoutInputText.show(true)
                edtSearch.requestFocus()
                spnFilter.show(false)
                toggleEdit(false)
            }
            actionSort -> {
                if (!rvMain.isVisible()) return
                adapterMain?.run {
                    if (itemCount < 2) return
                    mLayoutInputText.show(false)
                    spnFilter.show(true)
                    sort()
                    toggleEdit(false)
                }
            }
            actionEdit -> {
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

    override fun submit() {
        Timber.e("-------submit------------------------------------")
        val adapter = if (rvMain.isVisible()) {
            adapterMain
        } else if (rvFilter.isVisible()) {
            adapterFilter
        } else null

        val items = adapter?.selectedItems()?.map { it.value }
        items?.run {
            Timber.e("------selectedItems--------$indices---------------------")
            if (isNotEmpty()) {
                selectedItems = this
                DialogUtil.showMessageConfirm(context, R.string.notifications, R.string.confirm_delete,
                        MaterialDialog.SingleButtonCallback { _, _ -> doIt() })
            } else {
                updateBtnSubmit(adapter)
            }
        } ?: let {
            updateBtnSubmit(adapter)
        }
    }

    private fun doIt() {
        Timber.e("-------doIt--------------------")
        undoBtn?.onUndo(false)
        onToggle()
    }

    override fun undo() {
        selectedItems?.run {
            undoBtn?.onUndo(true)
            onToggle()
        }
    }

    override fun isDataEmpty(): Boolean {
        adapterMain?.run {
            return itemCount == 0
        }
        return true
    }

    override fun adapterClicked(ad: BaseRvRealmAdapter<V>, position: Int, f: (() -> Unit)?) {
        ad.itemsSelectable?.run {
            allSelected = ad.onSelected(position)
        } ?: let {
            f?.invoke()
        }
    }

    override fun onClick(position: Int, event: Int) {
        Timber.e("---------------------onClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                adapterMain?.run {
                    adapterClicked(this, position)
                }
            }
            TYPE_ITEM_FILTER -> {
                adapterFilter?.run {
                    adapterClicked(this, position)
                }
            }
        }
    }

    override fun onLongClick(position: Int, event: Int) {
        Timber.e("---------------------onLongClick--------------------$position")
        when (event) {
            TYPE_ITEM -> {
                adapterMain?.run {
                    allSelected = onSelected(position)
                }
            }
            TYPE_ITEM_FILTER -> {
                adapterFilter?.run {
                    allSelected = onSelected(position)
                }
            }
        }
    }
}