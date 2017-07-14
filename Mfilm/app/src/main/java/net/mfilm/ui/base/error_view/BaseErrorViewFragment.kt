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
        errorView?.run {
            fun retry() {
                onErrorViewRetry(this, { onErrorViewDemand(this) })
            }
            setOnClickListener { retry() }
            setOnRetryListener { retry() }
            subTitle?.run {
                subtitle = getString(this)
            }
        }
    }

    override fun onErrorViewRetry(errorView: ErrorView?, f: () -> Unit) {
        Timber.e("--------------onErrorViewRetry--------------")
        errorView?.run {
            show(false)
            f()
        }
    }

    override fun showErrorView(show: Boolean, f: (() -> Unit)?): Boolean {
        hideLoading()
        Timber.e("-----showErrorView---1--$show--------$errorView---------------")
        fun show() {
            Timber.e("-----showErrorView--2---$show--------$errorView---------------")
//            errorView?.apply { show(show) } ?: f?.invoke()
            errorView?.show(show)
        }
        if (show) {
            if (isDataEmpty()) {
                show()
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