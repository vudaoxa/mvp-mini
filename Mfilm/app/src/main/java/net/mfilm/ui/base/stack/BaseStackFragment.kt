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
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View
import com.google.android.gms.ads.InterstitialAd
import net.mfilm.di.components.ActComponent
import net.mfilm.google.IAdListener
import net.mfilm.google.ads
import net.mfilm.google.initInterAds
import net.mfilm.google.requestNewInterstitial
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.ICallbackFragmentOptionMenu
import net.mfilm.utils.anim
import net.mfilm.utils.handler
import net.mfilm.utils.isVisiOk
import timber.log.Timber
import vn.tieudieu.fragmentstackmanager.BaseFragmentStack

/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseStackFragment : BaseFragmentStack(), MvpView, ICallbackFragmentOptionMenu {

    var baseActivity: BaseStackActivity? = null
        private set

    override val optionsMenuId: Int
        get() = -1

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

    override fun setScrollToolbarFlag(info: Boolean) {
        baseActivity?.setScrollToolbarFlag(info)
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
        get() = baseActivity?.isNetworkConnected ?: false

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }

    override fun hideKeyboard() {
        baseActivity?.hideKeyboard()
    }

    override fun openActivityOnTokenExpire() {
        baseActivity?.openActivityOnTokenExpire()
    }

    val activityComponent: ActComponent?
        get() = baseActivity?.activityComponent

    fun attachChildFragment(view: View, @IdRes containerId: Int, fragment: Fragment?) {
        if (fragment == null || fragment.isVisiOk()) {
            return
        }
        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        handler({
            ft.add(containerId, fragment)
            view.startAnimation(anim)
            view.postOnAnimationDelayed({ ft.commit() }, 250)
        })
    }

    fun removeChildFragment(@IdRes containerId: Int) {
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

    //inter ads
    protected var mInterAd: InterstitialAd? = null
    protected var mPosition = 0
    protected var mEvent = 0
    private var action: (() -> Unit)? = null
    protected fun capture(position: Int, event: Int) {
        mPosition = position
        mEvent = event
    }

    protected open fun initAds() {
        adListener = object : IAdListener() {
            override fun fClosed() {
                requestNewInterstitial(mInterAd)
                //to avoid onSaveInstantState exception
                handler({
                    action?.invoke()
                })
            }

            override fun fFailedToLoaded() {

            }

            override fun fLoaded() {

            }
        }
        adListener?.run {
            mInterAd = initInterAds(context, this)
        }
    }

    protected fun interAds(page: Int? = null, f: (() -> Unit)? = null) {
        action = f
        page?.run {
            Timber.e("----interAds------------page--------$page--------")
            if (this % pagesPerAds == 0)
                ads(f)
        } ?: let {
            ads(f)
        }
    }

    protected var pagesPerAds = 2
    private fun ads(f: (() -> Unit)? = null) {
        ads(mInterAd, f)
    }

    private var adListener: IAdListener? = null
}
