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

//import net.mfilm.ui.login.LoginActivity

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import net.mfilm.R
import net.mfilm.di.components.ActComponent
import net.mfilm.ui.base.BaseFragment
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.videos.model.MPlayer
import net.mfilm.utils.isNetConnected
import net.mfilm.utils.showLoadingDialog
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseStackActivity : BaseActivityFragmentStack(), MvpView, BaseFragment.Callback {
    private var mProgressDialog: ProgressDialog? = null
    lateinit var activityComponent: ActComponent
        private set
//    private var mUnBinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        activityComponent = D.builder()
//                .activityModule(ActModule(this))
//                .applicationComponent(MApplication.instance.mAppComponent)
//                .build()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissionsSafely(permissions: Array<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun showLoading() {
        hideLoading()
        mProgressDialog = showLoadingDialog(this)
    }

    override fun hideLoading() {
        mProgressDialog?.apply {
            if (!isShowing)
                cancel()
        }
    }

    override fun onError(message: String?) {
        message?.apply {
            showSnackBar(this)
        } ?: let {
            showSnackBar(getString(R.string.some_error))
        }
    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT)
        val sbView = snackbar.view
        val textView = sbView
                .findViewById(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackbar.show()
    }

    override fun onError(@StringRes resId: Int) {
        onError(getString(resId))
    }

    override val isNetworkConnected: Boolean
        get() = isNetConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun openActivityOnTokenExpire() {
//        startActivity(LoginActivity.getStartIntent(this))
//        finish()
    }

    abstract fun playVideo(mPlayer: MPlayer?)
}
