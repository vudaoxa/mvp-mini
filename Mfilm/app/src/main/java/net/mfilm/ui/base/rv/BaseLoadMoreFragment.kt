package net.mfilm.ui.base.rv

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.ALoadMore
import net.mfilm.utils.ICallbackLoadMore

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseLoadMoreFragment : BaseStackFragment(), ICallbackLoadMore {
    var _pape = 1
    override var page: Int
        get() = _pape
        set(value) {
            _pape = value
        }

    init {
        reset()
    }

    override fun reset() {
        page = 1
    }

    val mCallBackLoadMore = object : ALoadMore({ onLoadMore() }) {
        override fun onLoadMore() {
//            if (mSearchRequest.nextPageToken == null) {
            if (isDataEnd) {
                return
            }
            f()
            countLoadmore++
            if (countLoadmore > 1) {
                hideKeyboard()
            }
        }
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore) {
        rv.addOnScrollListener(
                object : EndlessRvScrollListener(rv.layoutManager as StaggeredGridLayoutManager) {
                    //                object : EndlessRvScrollListener(rv.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        mCallbackLoadMore.onLoadMore()
                    }
                })
    }
}