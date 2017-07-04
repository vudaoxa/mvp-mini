package net.mfilm.ui.base.error_view

import net.mfilm.R
import net.mfilm.utils.ICallbackRefresh
import net.mfilm.utils.handler


/**
 * Created by MRVU on 6/6/2017.
 */
abstract class BasePullRefreshFragment : BaseErrorViewFragment(), ICallbackRefresh {
    override fun initViews() {
        super.initViews()
        initSwipe()
    }

    override fun initSwipe() {
        swipeContainer?.run {
            if (pullToRefreshEnabled()) {
                setOnRefreshListener { onRefresh() }
                setColorSchemeResources(*pullToRefreshColorResources)
            } else {
                isEnabled = false
            }
        }
    }

    override fun pullToRefreshEnabled() = swipeContainer != null
    override val pullToRefreshColorResources: IntArray
        get() = intArrayOf(R.color.colorPrimary, R.color.blue, R.color.green)

    override fun onRefresh() {
        handler({
            setRefreshing(false)
            if (!isRefreshed) {
                onErrorViewDemand(errorView)
                isRefreshed = true
            }
        }, 2000)
    }

    override fun setRefreshing(refreshing: Boolean) {
        swipeContainer?.run {
            isRefreshing = refreshing
        }
    }

    override fun setRefreshed(refreshed: Boolean, f: (() -> Unit)?) {
        if (!isRefreshed) return
        isRefreshed = refreshed
        f?.invoke()
    }

    private var isRefreshed = false
}