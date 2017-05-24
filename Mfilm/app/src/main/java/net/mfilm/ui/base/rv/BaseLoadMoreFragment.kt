package net.mfilm.ui.base.rv

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.ALoadMore
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
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?) {
        rv.addOnScrollListener(
                object : EndlessRvScrollListener(rv.layoutManager as StaggeredGridLayoutManager) {
                    //                object : EndlessRvScrollListener(rv.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        mCallbackLoadMore?.onLoadMore()
                    }
                })
    }
}