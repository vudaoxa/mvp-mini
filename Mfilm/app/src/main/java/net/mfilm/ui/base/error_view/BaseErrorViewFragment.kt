package net.mfilm.ui.base.error_view

import net.mfilm.ui.base.stack.BaseStackFragment
import net.mfilm.utils.ICallbackErrorView
import net.mfilm.utils.show
import timber.log.Timber
import tr.xip.errorview.ErrorView

/**
 * Created by MRVU on 6/5/2017.
 */
abstract class BaseErrorViewFragment : BaseStackFragment(), ICallbackErrorView {
    override fun initViews() {
        initErrorView(errorView, subTitle)
    }

    override fun initErrorView(errorView: ErrorView?, subTitle: Int?) {
        errorView?.apply {
            fun retry() {
                onErrorViewRetry(this, { onErrorViewDemand(this) })
            }
            setOnClickListener { retry() }
            setOnRetryListener { retry() }
            subTitle?.apply {
                subtitle = getString(this)
            }
        }
    }

    override fun onErrorViewRetry(errorView: ErrorView?, f: () -> Unit) {
        Timber.e("--------------onErrorViewRetry--------------")
        errorView?.apply {
            show(false)
            f()
        }
    }

    override fun showErrorView(show: Boolean, f: (() -> Unit)?): Boolean {
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
            //hide error
            show()
        }
        //show super onFailure
        f?.invoke()
        return false
    }

    override fun onFailure() {
        showErrorView(true, { super.onFailure() })
        loadInterrupted()
    }

    override fun onNoInternetConnections() {
        showErrorView(true, { super.onNoInternetConnections() })
        loadInterrupted()
    }

    override fun loadInterrupted() {
        Timber.e("------------loadInterrupted--------------")
    }
}