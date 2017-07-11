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

/**
 * Created by janisharali on 27/01/17.
 */

import android.support.annotation.StringRes

/**
 * Base interface that any class that wants to act as a View in the MVP (Model View Presenter)
 * pattern must implement. Generally this interface will be extended by a more specific interface
 * that then usually will be implemented by an MainActivity or Fragment.
 */
interface MvpView {
    fun showLoading()
    fun hideLoading()
    fun openActivityOnTokenExpire()
    fun onError(@StringRes resId: Int)
    fun onError(message: String?)
    val isNetworkConnected: Boolean
    fun onNoInternetConnections()
    fun onFailure()
    fun hideKeyboard()
}
