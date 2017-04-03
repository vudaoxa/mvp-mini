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


//import net.mfilm.data.networkretrofit.models.AccessToken
//import net.mfilm.ui.login.LoginPresenter

import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataMng
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject

/**
 * Created by janisharali on 27/01/17.
 */

class SplashPresenter<V : SplashMvpView> @Inject
constructor(dataManager: DataMng, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), SplashMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)

        mvpView.startSyncService()
        checkToken()
//        compositeDisposable.add(dataManager.seedDatabaseQuestions()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .concatMap { aBoolean ->
//                    Log.d(TAG, "xyz--concatMap-apply--" + aBoolean!!)
//                    dataManager.seedDatabaseOptions()
//                }
//                .subscribe(Consumer<Boolean> { aBoolean ->
//                    Log.d(TAG, "xyz--subscribe-accept--" + aBoolean!!)
//
//                    if (!isViewAttached) {
//                        return@Consumer
//                    }
//                    decideNextActivity()
//                }, Consumer<Throwable> { throwable ->
//                    Log.d(TAG, "xyz--Consumer-accept--" + throwable.message)
//                    if (!isViewAttached) {
//                        return@Consumer
//                    }
//                    mvpView.onError(R.string.some_error)
//                    decideNextActivity()
//                }))
    }

    fun checkToken() {
//        mvpView?.apply {
//            showLoading()
//            val d = object : DisposableObserver<AccessToken>() {
//                override fun onComplete() {
//                    openMainActivity()
//                }
//
//                override fun onError(e: Throwable?) {
//                    when (e) {
//                        is HttpException -> {
//                            DebugLog.e("connect failed---------")
//                        }
//                        is IOException -> {
//                            DebugLog.e("no internet connection ----------------")
//                        }
//                    }
//                }
//
//                override fun onNext(t: AccessToken?) {
//                    if (isViewAttached) {
//                        openMainActivity()
//                        val tk = t?.accessToken
//                        dataManager.accessToken = tk
//                        dataManager.currentUserLoggedInMode = DataMng.LoggedInMode.LOGGED_IN_MODE_SERVER_DEFAULT.type
//                        DebugLog.e("tk-----------------$tk")
//                    }
//                }
//            }
//            dataManager.getAccessTokenAndLogin(NetConstants.DEFAULT_USERNAME, NetConstants.PASSWORD, null)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(d)
//            compositeDisposable.add(d)
//        }
    }

//    private fun decideNextActivity() {
//        if (dataManager.currentUserLoggedInMode == DataMng.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.type) {
//            mvpView!!.openLoginActivity()
//        } else {
//            mvpView!!.openMainActivity()
//        }
//    }

    companion object {

        private val TAG = "SplashPresenter"
    }
}
