/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package  net.mfilm.ui.base.stack

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import net.mfilm.di.components.ActComponent
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.anim
import net.mfilm.utils.handler
import vn.tieudieu.fragmentstackmanager.BaseFragmentStack

/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseStackFragment : BaseFragmentStack(), MvpView {

    var baseActivity: BaseStackActivity? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        initViews()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseStackActivity) {
            this.baseActivity = context
            context.onFragmentAttached()
        }
    }

    override fun showLoading() {
        baseActivity?.showLoading()
    }

    override fun hideLoading() {
        baseActivity?.hideLoading()
    }

    override fun onFailure() {
        baseActivity?.onFailure()
    }

    override fun onNoInternetConnections() {
        baseActivity?.onNoInternetConnections()
    }
    override fun onError(message: String?) {
        baseActivity?.onError(message)
    }

    override fun onError(@StringRes resId: Int) {
        baseActivity?.onError(resId)
    }

    override val isNetworkConnected: Boolean
        get() {
            if (baseActivity != null) {
                return baseActivity!!.isNetworkConnected
            }
            return false
        }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun hideKeyboard() {
        if (baseActivity != null) {
            baseActivity!!.hideKeyboard()
        }
    }

    override fun openActivityOnTokenExpire() {
        baseActivity?.openActivityOnTokenExpire()
    }

    val activityComponent: ActComponent
        get() = baseActivity!!.activityComponent

    fun attachChildFragment(view: View, containerId: Int, fragment: Fragment?) {
        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        if (fragment == null || fragment.isAdded || fragment.isInLayout) {
            return
        }
        handler({
            ft.add(containerId, fragment)
            view.startAnimation(anim)
            view.postOnAnimationDelayed({ ft.commit() }, 250)
        })
    }

    fun removeChildFragment(containerId: Int) {
        // remove
        try {
            val fm = childFragmentManager
            val fragment = fm.findFragmentById(containerId) ?: return
            if (!fragment.isAdded) return
            val ft = fm.beginTransaction()
            ft.remove(fragment)
            ft.commit()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    protected abstract fun initViews()

    protected abstract fun initFields()

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
