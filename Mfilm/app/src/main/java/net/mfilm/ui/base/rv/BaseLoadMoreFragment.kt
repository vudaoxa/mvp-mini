package net.mfilm.ui.base.rv

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
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        DebugLog.e("------------onLoadMore($page, $totalItemsCount)----------------")
                        mCallbackLoadMore?.onLoadMore()
                    }

                    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(view, dx, dy)
                        DebugLog.e("--------------onScrolled($dx, $dy)--------------")
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        DebugLog.e("--------------onScrollStateChanged($newState)--------------")
                    }
                })
    }

    fun nullByAdapter(adapterExisted: Boolean) {
        DebugLog.e("------------------------nullByAdapter----------$adapterExisted---------")
    }
}