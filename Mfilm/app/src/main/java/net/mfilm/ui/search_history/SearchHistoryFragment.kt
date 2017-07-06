package net.mfilm.ui.search_history

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.bottom_fun_view.*
import kotlinx.android.synthetic.main.fragment_search_history.*
import kotlinx.android.synthetic.main.item_history_clear.*
import net.mfilm.R
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.realm.BaseMiniRealmFragment
import net.mfilm.ui.base.rv.wrappers.LinearLayoutManagerWrapper
import net.mfilm.ui.custom.SimpleViewAnimator
import net.mfilm.ui.manga.UndoBtn
import net.mfilm.ui.manga.rv.BaseRvRealmAdapter
import net.mfilm.ui.mangas.search.SearchHistoryMvpPresenter
import net.mfilm.utils.isVisible
import net.mfilm.utils.show
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by MRVU on 7/6/2017.
 */
class SearchHistoryFragment : BaseMiniRealmFragment<SearchQueryRealm>(), SearchHistoryMvpView {
    companion object {
        private var mSearchHistoryFragment: SearchHistoryFragment? = null
        fun getInstance(): SearchHistoryFragment {
            return mSearchHistoryFragment?.run { this } ?: newInstance()
        }

        fun newInstance(): SearchHistoryFragment {
            val fragment = SearchHistoryFragment()
            mSearchHistoryFragment = fragment
            return fragment
        }
    }

    override fun onToggle() {
        selectedItems?.run {
            mSearchHistoryPresenter.toggleSearchHistory(this)
        }
    }

    override val rvMain: RecyclerView
        get() = rv_search_history
    override var adapterMain: BaseRvRealmAdapter<SearchQueryRealm>?
        get() = mSearchQueryRvAdapter
        set(value) {
            mSearchQueryRvAdapter = value
        }
    lateinit var mSearchHistoryLayoutManager: LinearLayoutManagerWrapper
    override var layoutManagerMain: RecyclerView.LayoutManager
        get() = mSearchHistoryLayoutManager
        set(value) {
            mSearchHistoryLayoutManager = value as LinearLayoutManagerWrapper
        }
    override val bottomFunView: SimpleViewAnimator
        get() = bottom_fun
    var mUndoBtn: UndoBtn? = null
    override var undoBtn: UndoBtn?
        get() = mUndoBtn
        set(value) {
            mUndoBtn = value
        }
    override val btnSelect: Button
        get() = btn_toggle_select
    override val btnUndo: Button
        get() = btn_undo
    override val btnSubmit: Button
        get() = btn_delete
    override val btnDone: Button
        get() = btn_done


    override fun isHistoryVisible(): Boolean {
        Timber.e("----isHistoryVisible--------rvMain--------$rvMain-----------------------")
        return rvMain.isVisible()
    }

    override fun show(show: Boolean) {
        rvMain.show(show)
    }

    @Inject
    lateinit var mSearchHistoryPresenter: SearchHistoryMvpPresenter<SearchHistoryMvpView>
    var mSearchQueryRvAdapter: BaseRvRealmAdapter<SearchQueryRealm>? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_search_history, container, false)
    }

    override fun initFields() {
        activityComponent.inject(this)
        mSearchHistoryPresenter.onAttach(this)
    }

    override fun initViews() {
        super.initViews()
        requestSearchHistory()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("----onDestroy------------------------------")
        mSearchHistoryPresenter.onDetach()
    }

    override fun requestSearchHistory() {
        mSearchHistoryPresenter.requestSearchHistory()
    }

    override fun done() {
        super.done()
        requestSearchHistory()
    }
    override fun onSearchHistoryResponse(searchHistoryRealms: List<SearchQueryRealm>?) {
        hideLoading()
        searchHistoryRealms.let { shr ->
            shr?.run {
                if (shr.isNotEmpty()) {
                    buildSearchHistory(shr)
                } else onSearchHistoryNull()
            } ?: let { onSearchHistoryNull() }
        }
    }

    override fun onSearchHistoryNull() {
        xx
        Timber.e("----------------onSearchHistoryNull------------------")
    }

    override fun buildSearchHistory(searchHistoryRealms: List<SearchQueryRealm>) {
        xx
        Timber.e("---------------buildSearchHistory---------------${searchHistoryRealms.size}")
        mSearchQueryRvAdapter?.run {
            clear()
            addAll(searchHistoryRealms)
            notifyDataSetChanged()
        } ?: let {
            mSearchQueryRvAdapter = BaseRvRealmAdapter(context, searchHistoryRealms.toMutableList(), this, this)
            rvMain.adapter = mSearchQueryRvAdapter
        }
    }

    override fun saveQuery(query: String) {
        mSearchHistoryPresenter.saveQuery(query)
    }

    override fun deleteAll(f: (() -> Unit)?) {
        super.deleteAll({
            mSearchHistoryPresenter.delete(SearchQueryRealm::class.java)
        })
    }
//    override fun onClick(position: Int, event: Int) {
//        Timber.e("---------------------onClick--------------------$position")
//        when (event) {
//            TYPE_ITEM_SEARCH_HISTORY -> {
//                mSearchQueryRvAdapter?.mData?.run {
//                    baseActivity?.onSearchHistoryClicked(get(position))
//                }
//            }
//        }
//    }
//
//    override fun onLongClick(position: Int, event: Int) {
//        Timber.e("---------------------onLongClick--------------------$position")
//    }

    override fun adapterClicked(ad: BaseRvRealmAdapter<SearchQueryRealm>, position: Int, f: (() -> Unit)?) {
        super.adapterClicked(ad, position, {
            ad.mData?.run {
                baseActivity?.onSearchHistoryClicked(get(position))
            }
        })
    }
}