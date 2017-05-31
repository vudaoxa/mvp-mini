package net.mfilm.ui.base.rv

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.ALoadMore
import net.mfilm.utils.DebugLog
import net.mfilm.utils.ICallbackLoadMore
import net.mfilm.utils.PAGE_START

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseLoadMoreFragment : BaseStackFragment(), ICallbackLoadMore {
    var _pape = PAGE_START
    override var page: Int
        get() = _pape
        set(value) {
            _pape = value
        }
    var mCallBackLoadMore: ALoadMore? = null
    private var mEndlessRvScrollListener: EndlessRvScrollListener? = null
    init {
        mCallBackLoadMore = object : ALoadMore({ onLoadMore() }) {
            override fun onLoadMore() {
                if (isDataEnd) {
                    return
                }
                f()
                countLoadMore++
                if (countLoadMore > 1) {
                    hideKeyboard()
                }
            }
        }
        reset()
    }

    override fun reset() {
        page = PAGE_START
        mCallBackLoadMore?.reset()
        mEndlessRvScrollListener?.reset()
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?) {
        when (rv.layoutManager) {
            is LinearLayoutManager -> {
                setupOnLoadMore(rv, mCallbackLoadMore, rv.layoutManager as LinearLayoutManager)
            }
            is StaggeredGridLayoutManager -> {
                setupOnLoadMore(rv, mCallbackLoadMore, rv.layoutManager as StaggeredGridLayoutManager)
            }
            is GridLayoutManager -> {
                setupOnLoadMore(rv, mCallbackLoadMore, rv.layoutManager as GridLayoutManager)
            }
        }
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?, linearLayoutManager: LinearLayoutManager) {
        mEndlessRvScrollListener = object : EndlessRvScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                DebugLog.e("-----LinearLayoutManager-------onLoadMore ($page, $totalItemsCount)----------------")
                mCallbackLoadMore?.onLoadMore()
            }
        }
        rv.addOnScrollListener(mEndlessRvScrollListener)
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?, gridLayoutManager: GridLayoutManager) {
        mEndlessRvScrollListener = object : EndlessRvScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                DebugLog.e("------GridLayoutManager------onLoadMore($page, $totalItemsCount)----------------")
                mCallbackLoadMore?.onLoadMore()
            }
        }
        rv.addOnScrollListener(mEndlessRvScrollListener)
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?, staggeredGridLayoutManager: StaggeredGridLayoutManager) {
        mEndlessRvScrollListener = object : EndlessRvScrollListener(staggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                DebugLog.e("------StaggeredGridLayoutManager------onLoadMore($page, $totalItemsCount)----------------")
                mCallbackLoadMore?.onLoadMore()
            }

//                    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
//                        super.onScrolled(view, dx, dy)
//                        DebugLog.e("--------------onScrolled($dx, $dy)--------------")
//                    }
//
//                    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                        DebugLog.e("--------------onScrollStateChanged($newState)--------------")
//                    }
        }
        rv.addOnScrollListener(mEndlessRvScrollListener)

    }

    fun nullByAdapter(adapterExisted: Boolean) {
        DebugLog.e("------------------------nullByAdapter----------$adapterExisted---------")
    }
}