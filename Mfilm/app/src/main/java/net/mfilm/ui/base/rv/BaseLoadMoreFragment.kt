package net.mfilm.ui.base.rv

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import net.mfilm.ui.base.error_view.BasePullRefreshFragment
import net.mfilm.utils.ALoadMore
import net.mfilm.utils.ICallbackLoadMore
import net.mfilm.utils.PAGE_START
import net.mfilm.utils.show
import timber.log.Timber

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseLoadMoreFragment : BasePullRefreshFragment(), ICallbackLoadMore {
    override fun initViews() {
        super.initViews()
        initErrorView(errorViewLoadMore, subTitleLoadMore)
    }

    override fun showErrorView(show: Boolean, f: (() -> Unit)?): Boolean {
        fun show(): Boolean {
            hideLoading()
            Timber.e("------------showErrorView--------- $errorViewLoadMore----------------------")
            errorViewLoadMore?.show(show) ?: return super.showErrorView(show, f)
            return true
        }
        if (show) {
            if (!isDataEmpty()) {
                //don't show super onFailure
                return show()
            }
        } else {
            //hide error
            return show()
        }
        //show super showErrorView
        return super.showErrorView(show, f)
    }

    private var _pape = PAGE_START
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

    override fun reset(f: (() -> Unit)?) {
        Timber.e("-------------------reset-----------------------")
        page = PAGE_START
        mCallBackLoadMore?.reset()
        mEndlessRvScrollListener?.reset()
        f?.invoke()
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
                Timber.e("-----LinearLayoutManager-------onLoadMore ($page, $totalItemsCount)----------------")
                mCallbackLoadMore?.onLoadMore()
            }
        }
        rv.addOnScrollListener(mEndlessRvScrollListener)
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?, gridLayoutManager: GridLayoutManager) {
        mEndlessRvScrollListener = object : EndlessRvScrollListener(gridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                Timber.e("------GridLayoutManager------onLoadMore($page, $totalItemsCount)----------------")
                mCallbackLoadMore?.onLoadMore()
            }
        }
        rv.addOnScrollListener(mEndlessRvScrollListener)
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ALoadMore?, staggeredGridLayoutManager: StaggeredGridLayoutManager) {
        mEndlessRvScrollListener = object : EndlessRvScrollListener(staggeredGridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                Timber.e("------StaggeredGridLayoutManager------onLoadMore($page, $totalItemsCount)----------------")
                mCallbackLoadMore?.onLoadMore()
            }

//                    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
//                        super.onScrolled(view, dx, dy)
//                        Timber.e("--------------onScrolled($dx, $dy)--------------")
//                    }
//
//                    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
//                        super.onScrollStateChanged(recyclerView, newState)
//                        Timber.e("--------------onScrollStateChanged($newState)--------------")
//                    }
        }
        rv.addOnScrollListener(mEndlessRvScrollListener)

    }

    override fun adapterEmpty(empty: Boolean) {
        Timber.e("------------------------adapterEmpty----------$empty---------")
    }

}