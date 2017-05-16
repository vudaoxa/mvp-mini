package net.mfilm.ui.base.rv

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.ALoadMore
import net.mfilm.utils.ICallbackLoadMore

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseLoadMoreFragment : BaseStackFragment(), ICallbackLoadMore {
    override var page: Int
        get() = page
        set(value) {
            page = value
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
            this@BaseLoadMoreFragment.onLoadMore()
        }
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore) {
        rv.addOnScrollListener(
                object : EndlessRvScrollListener(rv.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        mCallbackLoadMore.onLoadMore()
                    }
                })
    }
}