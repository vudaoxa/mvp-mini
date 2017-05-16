package net.mfilm.utils

/**
 * Created by tusi on 5/16/17.
 */
interface ICallbackOnClick {
    fun onClick(position: Int, event: Int)
}

abstract class ALoadMore(val f: () -> Unit) {
    var countLoadmore: Int = 0
    //    abstract fun onLoadMore(f: ()->Unit)
    abstract fun onLoadMore()
}

interface ICallbackLoadMore {
    var isDataEnd: Boolean
    var page: Int
    fun onLoadMore()
    fun reset()
}

interface IAdapterLoadMore {
    fun onLoadMore()
    fun reset()
    fun onLoadMoreFinished()
}
interface ICallbackRefresh {
    fun setRefreshing(refreshing: Boolean)
    fun pullToRefreshEnabled(): Boolean
    val pullToRefreshColorResources: IntArray
    fun onRefresh()
    fun initSwipe()
}