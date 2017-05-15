package net.mfilm.utils

/**
 * Created by tusi on 5/16/17.
 */
interface ICallbackOnClick {
    fun onClick(position: Int, event: Int)
}

interface ICallbackRefresh {
    fun setRefreshing(refreshing: Boolean)
    fun pullToRefreshEnabled(): Boolean
    val pullToRefreshColorResources: IntArray
    fun onRefresh()
    fun initSwipe()
}