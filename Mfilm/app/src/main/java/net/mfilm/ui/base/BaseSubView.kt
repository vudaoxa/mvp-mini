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

package net.mfilm.ui.base

import android.annotation.TargetApi
import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseSubView : ViewGroup, SubMvpView {

    private var mParentMvpView: MvpView? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun attachParentMvpView(mvpView: MvpView) {
        mParentMvpView = mvpView
    }

    override fun showLoading() {
        mParentMvpView?.showLoading()
    }

    override fun hideLoading() {
        mParentMvpView?.hideLoading()
    }

    override fun onError(@StringRes resId: Int) {
        mParentMvpView?.onError(resId)
    }

    override fun onError(message: String?) {
        mParentMvpView?.onError(message)
    }

    override fun hideKeyboard() {
        mParentMvpView?.hideKeyboard()
    }

    override val isNetworkConnected: Boolean
        get() {
            if (mParentMvpView != null) {
                return mParentMvpView!!.isNetworkConnected
            }
            return false
        }

    override fun openActivityOnTokenExpire() {
        mParentMvpView?.openActivityOnTokenExpire()
    }

    protected abstract fun bindViewsAndSetOnClickListeners()

    protected abstract fun setUp()
}
