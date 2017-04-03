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

package net.mfilm.ui.base.stack

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseFragmentStack

import net.mfilm.di.components.ActComponent
import net.mfilm.ui.base.MvpView


/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseStackFragment : BaseFragmentStack(), MvpView {

    var baseActivity: BaseStackActivity? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initField()
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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

    protected abstract fun initView()

    protected abstract fun initField()

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}
