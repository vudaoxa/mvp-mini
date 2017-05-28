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

import android.annotation.TargetApi
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import net.mfilm.MApplication
import net.mfilm.R
import net.mfilm.di.components.ActComponent
import net.mfilm.di.components.DaggerActComponent
import net.mfilm.di.modules.ActModule
import net.mfilm.ui.base.BaseFragment
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import vn.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import vn.tieudieu.fragmentstackmanager.BaseFragmentStack

/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseStackActivity : BaseActivityFragmentStack(), MvpView, BaseFragment.Callback, ICallbackToolbar {
    private var mProgressDialog: ProgressDialog? = null
    lateinit var activityComponent: ActComponent
        private set
    var mMenu: Menu? = null
    var mToggle: ActionBarDrawerToggle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.colorPrimary)
        }
        activityComponent = DaggerActComponent.builder()
                .actModule(ActModule(this))
                .appComponent(MApplication.instance.mAppComponent)
                .build()
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
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun showLoading() {
        hideLoading()
        mProgressDialog = showLoadingDialog(this)
    }

    override fun hideLoading() {
        mProgressDialog?.apply {
            if (isShowing)
                cancel()
        }
    }

    override fun onNoInternetConnections() {
        onError(R.string.internet_no_conection)
    }

    override fun onFailure() {
        onError(R.string.error_conection)
    }

    override fun onError(message: String?) {
        DebugLog.e(message)
        hideLoading()
        MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, message!!)
    }

    override fun onError(@StringRes resId: Int) {
//        onError(getString(resId))
        DebugLog.e(getString(resId))
        hideLoading()
        MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, resId)
    }

    override val isNetworkConnected: Boolean
        get() = isNetConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun onFragmentEntered(fragment: Fragment?) {
        setToolbarTitle((fragment as BaseFragmentStack).title)
        fragment.apply {
            if (fullScreen) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
                if (back) {
                    showBtnBack()
                } else {
                    showDrawer()
                }
            }
        }
    }
    override fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun openActivityOnTokenExpire() {
//        startActivity(LoginFragment.getStartIntent(this))
//        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            actionSettingsId -> {
                onSettings()
            }
            actionAboutId -> {
                onAbout()
            }
        }
        return true
    }

    fun setToolbarTitle(title: String?) {
        mToolbarTitle.text = if (!TextUtils.isEmpty(title)) title else getString(R.string.app_name)
    }

    fun showBtnBack(visi: Boolean) {
        mToolbarBack.show(visi)
    }

    fun showBtnBack() {
        mToolbarBack.show(true)
        mToolbar.navigationIcon = null
    }
    protected fun showDrawer() {
//        supportActionBar?.show()
        mToolbarBack.visibility = gone
        syncStateDrawer()
    }

    protected fun syncStateDrawer() {
        mDrawerLayout.post({ mToggle?.syncState() })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(optionsMenuId, menu)
        mMenu = menu
        return true
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setSupportActionBar(mToolbar)
        mToggle = ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mToggle?.apply {
            mDrawerLayout.addDrawerListener(this)
            syncState()
        }
        mToolbarBack.setOnClickListener { onBackPressed() }
        mBtnSearch.setImageDrawable(icon_search)
        mBtnFollow.setImageDrawable(icon_star)
        mBtnShare.setImageDrawable(icon_share)
        mBtnSearch.setOnClickListener { onSearch() }
        mBtnFollow.setOnClickListener { onFollow() }
        mBtnSearch.setOnClickListener { onShare() }
    }
}
