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
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import net.mfilm.MApplication
import net.mfilm.R
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.di.components.ActComponent
import net.mfilm.di.components.DaggerActComponent
import net.mfilm.di.modules.ActModule
import net.mfilm.ui.base.BaseFragment
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.manga.PassByTime
import net.mfilm.utils.*
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import vn.tieudieu.fragmentstackmanager.BaseActivityFragmentStack
import java.util.concurrent.TimeUnit

/**
 * Created by janisharali on 27/01/17.
 */

abstract class BaseStackActivity : BaseActivityFragmentStack(), MvpView, BaseFragment.Callback, ICallbackToolbar {
    private var mProgressDialog: ProgressDialog? = null
    lateinit var activityComponent: ActComponent
        private set
    var mMenu: Menu? = null
    var mToggle: ActionBarDrawerToggle? = null
    private var mSearchPassByTime: PassByTime? = null
    override var searchPassByTime: PassByTime?
        get() = mSearchPassByTime
        set(value) {
            mSearchPassByTime = value
        }
    private var mOptionsMenuId = -1
    override var optionsMenu: Int
        get() = mOptionsMenuId
        set(value) {
            mOptionsMenuId = value
        }

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
        mProgressDialog?.run {
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
        Timber.e(message)
        hideLoading()
        MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, message!!)
    }

    override fun onError(@StringRes resId: Int) {
//        onError(getString(resId))
        Timber.e(getString(resId))
        hideLoading()
        MApplication.instance.showMessage(AppConstants.TYPE_TOAST_ERROR, resId)
    }

    override val isNetworkConnected: Boolean
        get() = isNetConnected(applicationContext)

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    override fun onSearch(search: Boolean) {
        Timber.e("-------------------onSearch-------------$search")
        /*
        * hide toggle menu --  1
        * hide toolbar title-- 1
        * hide btns----1
        * hide options menu----1
        * hide rootview-----1
        * show btn back--1
        * */

        mToolbarTitle.show(!search)
        mLayoutInputText.show(search)
        if (search) {
            edtSearch.requestFocus()
        } else {
            hideKeyboard()
        }
    }

    override fun onFragmentEntered(f: Fragment?) {
        Timber.e("----onFragmentEntered-----$f---------------------------")
        f as BaseStackFragment
        f.run {
            if (fullScreen) {
                supportActionBar?.hide()
            } else {
                setToolbarTitle(f.title)
                Timber.e("---------mLayoutBtnsInfo.show($info)-------------")
                mLayoutBtnsInfo.show(info)
                setScrollToolbarFlag(info)
                mBtnSearch.show(searchable)
                showOptionsMenu(optionsMenuId)
                if (back) {
                    showBtnBack()
                } else {
                    showDrawer()
                }
                onSearch(searching)
                supportActionBar?.show()
            }
        }
    }

    override fun setScrollToolbarFlag(info: Boolean) {
        Timber.e("---setScrollToolbarFlag--------------$info----------------------------")
        val params = mToolbar.layoutParams as (AppBarLayout.LayoutParams)
        params.scrollFlags = if (info) 0 else {
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
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
        Timber.e("---------onOptionsItemSelected--------${item.itemId}--------------------")
        when (item.itemId) {
            actionSettingsId -> {
                onSettings()
            }
            actionAboutId -> {
                onAbout()
            }
            else -> {
                sendOptionsMenuItem(item)
            }
        }
        return true
    }

    override fun setToolbarTitle(title: String?) {
        mToolbarTitle.text = if (!TextUtils.isEmpty(title)) title else getString(R.string.app_name)
    }

    override fun showBtnBack(visi: Boolean) {
        mToolbarBack.show(visi)
    }

    override fun showBtnBack() {
        mToolbarBack.show(true)
        hideDrawerToggle()
    }

    override fun hideDrawerToggle() {
        handler({
            mToolbar.run {
                while (navigationIcon != null) {
                    navigationIcon = null
                }
            }
        })
    }

    override fun showOptionsMenu(show: Boolean) {
        mMenu?.run {
            if (!show) {
                clear()
            }
        }
    }

    override fun showOptionsMenu(optionsMenuId: Int) {
        optionsMenu = optionsMenuId
        if (optionsMenu != -1)
            invalidateOptionsMenu()
        else {
            showOptionsMenu(false)
        }
    }

    override fun showDrawer() {
        mToolbarBack.visibility = gone
        syncStateDrawer()
    }

    override fun syncStateDrawer() {
        mDrawerLayout.post({ mToggle?.syncState() })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Timber.e("----onCreateOptionsMenu--------$optionsMenu--------------------")
        if (optionsMenu == -1) return false
        menuInflater.inflate(optionsMenu, menu)
        mMenu = menu
        return true
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setSupportActionBar(mToolbar)
        mToggle = ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mToggle?.run {
            mDrawerLayout.addDrawerListener(this)
            syncState()
        }
        mToolbarBack.setOnClickListener { onBackPressed() }
        initSearch()
        mBtnFollow.setImageDrawable(icon_star)
        mBtnShare.setImageDrawable(icon_share)
        mBtnSearch.setOnClickListener { onSearchScreenRequested() }
        mBtnFollow.setOnClickListener { onFollow() }
        mBtnShare.setOnClickListener { onShare() }
    }

    override fun initSearch() {
        mBtnSearch.setImageDrawable(icon_search)
        initSearchPassByTime()
        initImeActionSearch()
        initRxSearch()
        initImgClear()
    }

    override fun initSearchPassByTime() {
        searchPassByTime = PassByTime(AUTO_LOAD_DURATION)
    }
    override fun initImeActionSearch() {
        edtSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                submitSearch()
                //searchTime to avoid conflict between searching and searching suggestion
                searchPassByTime?.run {
                    time = System.currentTimeMillis()
                }
            }
            false
        }
    }

    override fun initRxSearch() {
        RxTextView.afterTextChangeEvents(edtSearch)
                .debounce(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->
                    searchPassByTime?.passByTime {
                        val s = tvChangeEvent.view().text.toString()
                        val text = s.trim { it <= ' ' }
                        var clearShowed = true
                        if (text.isNotEmpty()) {
                            submitSearchSuggestion(text)
                        } else {
                            clearShowed = false
                        }
                        imgClear.show(clearShowed)
                    }
                }
    }

    override fun initImgClear() {
        imgClear.setOnClickListener {
            edtSearch.text = null
        }
    }
    override fun onSearchHistoryClicked(searchQueryRealm: SearchQueryRealm) {
        edtSearch.setText(searchQueryRealm.query)
//        sendHit(CATEGORY_ACTION, ACTION_CLICK_SEARCH_HISTORY_ITEM)
    }

    override fun submitSearchSuggestion(query: String) {
        Timber.e("submitSearchSuggestion--------------------------------" + query)
        //send to MainSearchFragment
        mCallbackSearchView?.onSearch(query)
//        sendHit(CATEGORY_ACTION, ACTION_SEARCH_SUGGESTION)
    }

    override fun submitSearch() {
//        sendHit(CATEGORY_ACTION, ACTION_SEARCH_SUBMIT)
        val query = edtSearch.text.toString().trim()
        if (query.isEmpty()) return
        mCallbackSearchView?.onSearch(query)
        hideKeyboard()
    }

    override fun onBackPressed() {
        Timber.e("----------------onBackPressed-----------------")
        if (mLayoutInputText.isVisible()) {
            mCallbackSearchView?.onBackPressed {
                onSearch(false)
                //pop fragment searching
                super.onBackPressed()
            }
        } else {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                fragmentStackManager.currentFragment?.run {
                    if (javaClass == homeClass) {
                        showConfirmExit()
                    } else super.onBackPressed()
                } ?: System.exit(0)
            }
        }
    }
}
