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

package net.mfilm.ui.splash

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import net.mfilm.R
import net.mfilm.data.DataMng
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.DebugLog
import net.mfilm.utils.handler
import javax.inject.Inject

/**
 * Created by janisharali on 27/01/17.
 */

class SplashPresenter<V : SplashMvpView> @Inject
constructor(dataManager: DataMng, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), SplashMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

//        mvpView.startSyncService()
        handler({ process() })
    }

    fun process() {
        compositeDisposable.add(
                dataManager.seedDatabaseQuestions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(Consumer<Boolean> { aBoolean ->
                            DebugLog.d("xyz--subscribe-accept--" + aBoolean!!)

                            if (!isViewAttached) {
                                return@Consumer
                            }
                            decideNextActivity()
                        }, Consumer<Throwable> { throwable ->
                            DebugLog.d("xyz--Consumer-accept--" + throwable.message)
                            if (!isViewAttached) {
                                return@Consumer
                            }
                            mvpView?.onError(R.string.some_error)
                            decideNextActivity()
                        }))
    }

    private fun decideNextActivity() {
        mvpView!!.openMainActivity()
    }

    companion object {

    }
}
