package net.mfilm.utils

/**
 * Created by tusi on 5/16/17.
 */
interface ICallbackOnClick {
    fun onClick(position: Int, event: Int)
}

abstract class ALoadMore(val f: () -> Unit) {
    protected var countLoadMore: Int = 0
    //    abstract fun onLoadMore(f: ()->Unit)
    abstract fun onLoadMore()

    fun reset() {
        countLoadMore = 0
    }
}

interface ICallbackLoadMore {
    var isDataEnd: Boolean
    var page: Int
    fun onLoadMore()
    fun reset()
}

interface IAdapterLoadMore {
    fun onAdapterLoadMore()
    fun onAdapterLoadMore(f: () -> Unit)
    fun reset()
    fun onAdapterLoadMoreFinished()
    fun onAdapterLoadMoreFinished(f: () -> Unit)
}
interface ICallbackRefresh {
    fun setRefreshing(refreshing: Boolean)
    fun pullToRefreshEnabled(): Boolean
    val pullToRefreshColorResources: IntArray
    fun onRefresh()
    fun initSwipe()
    fun reset()
}