package net.mfilm.ui.history

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import kotlinx.android.synthetic.main.bottom_fun_view.*
import kotlinx.android.synthetic.main.empty_data_view.*
import kotlinx.android.synthetic.main.fragment_realm.*
import kotlinx.android.synthetic.main.input_text.*
import kotlinx.android.synthetic.main.mini_switch_btns.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.realm.BaseRealmFragment
import net.mfilm.ui.base.rv.wrappers.StaggeredGridLayoutManagerWrapper
import net.mfilm.ui.custom.SimpleViewAnimator
import net.mfilm.ui.manga.EmptyDataView
import net.mfilm.ui.manga.Filter
import net.mfilm.ui.manga.PassByTime
import net.mfilm.ui.manga.UndoBtn
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
import net.mfilm.utils.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by tusi on 6/25/17.
 */
class HistoryFragment : BaseRealmFragment<MangaHistoryRealm>(), HistoryMvpView {
    companion object {
        fun newInstance(): HistoryFragment {
            val fragment = HistoryFragment()
            return fragment
        }
    }

    override val layoutPagerBtns: View
        get() = layout_pager_btns

    override val btns: List<View>
        get() = listOf(btn_newest, btn_all)

    private var mSearchPassByTime: PassByTime? = null
    override var searchPassByTime: PassByTime?
        get() = mSearchPassByTime
        set(value) {
            mSearchPassByTime = value
        }
    override var adapterFilter: BaseRvRealmAdapter<MangaHistoryRealm>?
        get() = mMangasFilterRvAdapter
        set(value) {
            mMangasFilterRvAdapter = value
        }
    override var adapterMain: BaseRvRealmAdapter<MangaHistoryRealm>?
        get() = mMangasRvAdapter
        set(value) {
            mMangasRvAdapter = value
        }
    override val bottomFunView: SimpleViewAnimator
        get() = bottom_fun
    override val btnSelect: Button
        get() = btn_toggle_select
    var mUndoBtn: UndoBtn? = null
    override var undoBtn: UndoBtn?
        get() = mUndoBtn
        set(value) {
            mUndoBtn = value
        }
    override val btnUndo: Button
        get() = btn_undo
    override val btnSubmit: Button
        get() = btn_delete
    override val btnDone: Button
        get() = btn_done
    override val mFilters: List<Filter>
        get() = filtersRealm
    //    override val spnFilter: NiceSpinner
//        get() = spn_filter
    override val mLayoutInputText: LinearLayout
        get() = layout_input_text
    override val edtSearch: EditText
        get() = edt_search
    override val imgClear: IconTextView
        get() = img_clear

    override var layoutManagerFilter: StaggeredGridLayoutManagerWrapper
        get() = mMangasRvFilterLayoutManagerWrapper
        set(value) {
            mMangasRvFilterLayoutManagerWrapper = value
        }
    override var layoutManagerMain: StaggeredGridLayoutManagerWrapper
        get() = mMangasRvLayoutManagerWrapper
        set(value) {
            mMangasRvLayoutManagerWrapper = value
        }

    override fun onToggle() {
        selectedItems?.run {
            mHistoryPresenter.toggleHistory(this)
        }
    }

    override fun deleteAll(f: (() -> Unit)?) {
        super.deleteAll({
            mHistoryPresenter.delete(MangaHistoryRealm::class.java)
        })
    }

    override val rvMain: RecyclerView
        get() = rv
    override val rvFilter: RecyclerView
        get() = rv_filter

    override fun search(query: String) {
        val favoritesFilter = adapterMain?.mData?.filter { it.name!!.contains(query, true) }
        favoritesFilter.let { ff ->
            ff?.run {
                if (ff.isNotEmpty()) {
                    buildHistoryFilter(ff)
                } else onRealmFilterNull()
            } ?: let { onRealmFilterNull() }
        }
    }

    override fun sort() {
        Timber.e("------------sort---------------------------")
        val filter = mFilters[pagerPosition]
        when (filter.content) {
            TYPE_FILTER_AZ -> {
                adapterMain?.run {
                    mData?.sortBy { it.name }
                    notifyDataSetChanged()
                }
            }
            TYPE_FILTER_TIME -> {
                adapterMain?.run {
                    mData?.sortByDescending { it.time }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override val actionSearch: Int
        get() = R.id.action_history_search
    override val actionSort: Int
        get() = R.id.action_history_sort
    override val actionEdit: Int
        get() = R.id.action_history_edit
    override val optionsMenuId: Int
        get() = R.menu.history
    override val layoutEmptyData: View?
        get() = layout_empty_data
    override val tvDesEmptyData: TextView?
        get() = tv_des
    override val emptyDesResId: Int
        get() {
            return R.string.empty_data_history
        }
    private var mEmptyDataView: EmptyDataView? = null
    override var emptyDataView: EmptyDataView?
        get() = mEmptyDataView
        set(value) {
            mEmptyDataView = value
        }
    override val spanCount: Int
        get() = resources.getInteger(R.integer.mangas_span_count)
    //    override val spnFilterTracker = AdapterTracker({
//        sort()
//    })
    @Inject
    lateinit var mHistoryPresenter: HistoryMvpPresenter<HistoryMvpView>
    lateinit var mMangasRvLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    lateinit var mMangasRvFilterLayoutManagerWrapper: StaggeredGridLayoutManagerWrapper
    var mMangasRvAdapter: BaseRvRealmAdapter<MangaHistoryRealm>? = null
    var mMangasFilterRvAdapter: BaseRvRealmAdapter<MangaHistoryRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_realm, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHistoryPresenter.onDetach()
    }

    override fun initFields() {
        searchable = true
        title = getString(R.string.history)
        tryOrExit {
            activityComponent?.inject(this)
            mHistoryPresenter.onAttach(this)
        }
    }

    override fun initViews() {
        super.initViews()
        requestHistory()
    }

    override fun done() {
        super.done()
        requestHistory()
    }

    override fun onHistoryEnabled(enabled: Boolean) {
        if (!enabled) {
            handler({
                showEmptyDataView(true, R.string.history_disabled)
                setScrollToolbarFlag(true)
            })
        }
    }
    override fun requestHistory() {
        mHistoryPresenter.requestHistory()
    }

    override fun onHistoryResponse(mangaHistoryRealms: List<MangaHistoryRealm>?) {
        hideLoading()
        mangaHistoryRealms?.run {
            if (isNotEmpty()) {
                buildHistory(this)
            } else onHistoryNull()
        } ?: let { onHistoryNull() }
    }

    override fun onHistoryNull() {
        Timber.e("----------------onHistoryNull------------------")
        adapterMain?.run {
            doByAllSelected(this)
        } ?: let {
            //set it after onFragmentEntered
            handler({
                showEmptyDataView(true)
                setScrollToolbarFlag(true)
            })
        }
    }

    override fun buildHistoryFilter(mangaHistoryRealms: List<MangaHistoryRealm>) {
        Timber.e("----------buildHistoryFilter-------- ${mangaHistoryRealms.size}----------------------------")
        adapterFilter?.run {
            doByAllSelected(this, mangaHistoryRealms)
        } ?: let {
            adapterFilter = BaseRvRealmAdapter(context, mangaHistoryRealms.toMutableList(), this, this, true)
            rvFilter.adapter = adapterFilter
        }
        showEmptyDataView(false)
        handler({
            rvMain.show(false)
            rvFilter.show(true)
        })
    }
    override fun buildHistory(mangaHistoryRealms: List<MangaHistoryRealm>) {
        Timber.e("---------------buildHistory--------$context-------${mangaHistoryRealms.size}")
        context ?: return
        setScrollToolbarFlag(false)
        adapterMain?.run {
            doByAllSelected(this, mangaHistoryRealms)
        } ?: let {
            //the first build
            adapterMain = BaseRvRealmAdapter(context, mangaHistoryRealms.toMutableList(), this, this)
            rvMain.adapter = adapterMain
            showEmptyDataView(false)
        }
    }

    override fun adapterClicked(ad: BaseRvRealmAdapter<MangaHistoryRealm>, position: Int, f: (() -> Unit)?) {
        super.adapterClicked(ad, position, {
            ad.mData?.run {
                screenManager?.onNewScreenRequested(IndexTags.FRAGMENT_MANGA_INFO,
                        typeContent = null, obj = this[position].id)
            }
        })
    }
}