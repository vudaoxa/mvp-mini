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


import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataManager
import net.mfilm.data.prefs.MangaSources
import net.mfilm.utils.ICallbackMangaSources
import net.mfilm.utils.PAGE_START
import net.mfilm.utils.mangaSources
import javax.inject.Inject

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * onAttach() and onDetach(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
open class BasePresenter<V : MvpView> @Inject
constructor(val dataManager: DataManager, val compositeDisposable: CompositeDisposable) : MvpPresenter<V>, ICallbackMangaSources {
    override val mMangaSources: MangaSources?
        get() = mangaSources
    var mvpView: V? = null
        private set

    protected var hl: String? = null
    fun obtainHl(page: Int) {
        if (page == PAGE_START) {
            hl = mMangaSources?.sources?.get(dataManager.mangaSourceIndex)?.code
        }
    }
    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        compositeDisposable.dispose()
        mvpView = null
    }

    fun checkViewAttached() {
        mvpView ?: throw MvpViewNotAttachedException()
    }

    override fun setUserAsLoggedOut() {
//        dataManager.accessToken = null
    }

    inner class MvpViewNotAttachedException : RuntimeException("Please call Presenter.onAttach(MvpView) before" + " requesting price to the Presenter")
}
