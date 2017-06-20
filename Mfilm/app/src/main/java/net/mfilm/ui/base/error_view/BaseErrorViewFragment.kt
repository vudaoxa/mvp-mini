package net.mfilm.ui.base.error_view

import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.ICallbackErrorView
import net.mfilm.utils.show
import timber.log.Timber

/**
 * Created by MRVU on 6/5/2017.
 */
abstract class BaseErrorViewFragment : BaseStackFragment(), ICallbackErrorView {
    override fun initViews() {
        initErrorView()
    }

    override fun initErrorView() {
        errorView?.apply {
            fun retry() {
                onErrorViewRetry({ onErrorViewDemand() })
            }
            setOnClickListener { retry() }
            setOnRetryListener { retry() }
            subTitle?.apply {
                subtitle = getString(this)
            }
        }
    }

    override fun onErrorViewRetry(f: () -> Unit) {
        Timber.e("--------------onErrorViewRetry--------------")
        errorView?.apply {
            show(false)
            f()
        }
    }

    override fun showErrorView(show: Boolean): Boolean {
        fun show() {
            hideLoading()
            errorView?.show(show)
        }
        if (show) {
            if (isDataEmpty()) {
                show()
                //don't show super onFailure
                return true
            }
        } else {
            show()
        }
        //show super onFailure
        return false
    }

    override fun onFailure() {
        if (!showErrorView(true))
            super.onFailure()
        loadFinished()
    }

    override fun onNoInternetConnections() {
        if (!showErrorView(true))
            super.onNoInternetConnections()
        loadFinished()
    }

    override fun loadFinished() {
        Timber.e("------------loadFinished--------------")
    }
}