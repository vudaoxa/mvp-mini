///*
// * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     https://mindorks.com/license/apache-v2
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License
// */
//
//package net.mfilm.ui.login
//
//
//import net.mfilm.R
//import net.mfilm.mangasPaging.DataManager
//import net.mfilm.mangasPaging.networkretrofit.models.AccessToken
//import net.mfilm.mangasPaging.networkretrofit.models.OnResponse
//import net.mfilm.mangasPaging.networkretrofit.support.NetConstants
//import net.mfilm.mangasPaging.networkretrofit.support.OnResponseSuccess
//import net.mfilm.ui.base.BasePresenter
//import net.mfilm.utils.CommonUtils
//import io.reactivex.android.schedulers.AndroidSchedulers
//
//import javax.inject.Inject
//
//import io.reactivex.disposables.CompositeDisposable
//import io.reactivex.observers.DisposableObserver
//import io.reactivex.schedulers.Schedulers
//import retrofit2.HttpException
//import java.io.IOException
//
///**
// * Created by janisharali on 27/01/17.
// */
//
//class LoginPresenter<V : LoginMvpView> @Inject
//constructor(dataManager: DataManager, compositeDisposable: CompositeDisposable) : BasePresenter<V>(dataManager, compositeDisposable), LoginMvpPresenter<V> {
//
//    override fun onServerLoginClick(email: String?, password: String?) {
//        //validate email and password
//        mvpView?.apply {
//            if (email == null || email.isEmpty()) {
//                onError(R.string.empty_email)
//                return
//            }
//            if (!CommonUtils.isEmailValid(email)) {
//                onError(R.string.invalid_email)
//                return
//            }
//            if (password == null || password.isEmpty()) {
//                onError(R.string.empty_password)
//                return
//            }
//            showLoading()
//        }
//
//    }
//
//    //assume for mainAct when open app,
//    //getUserInfo, if it's ok, go ahead
//    //else refresh token and continue
//    override fun onGoogleLoginClick() {
//        // instruct LoginActivity to initiate google login
////        mvpView?.apply {
////            showLoading()
////            val d = object :DisposableObserver<AccessToken>(){
////                override fun onComplete() {
////
////                }
////
////                override fun onError(e: Throwable?) {
////                    when(e){
////                        is HttpException->{
////                            Timber.e("connect failed---------")
////                        }
////                        is IOException->{
////                            Timber.e("no internet connection ----------------")
////                        }
////                    }
////                }
////
////                override fun onNext(t: AccessToken?) {
////                    if (isViewAttached){
////                        openMainActivity()
////                        val tk = t?.accessToken
////                        dataManager.accessToken=tk
////                        dataManager.currentUserLoggedInMode = DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER_DEFAULT.type
////                        Timber.e("tk-----------------$tk")
////                    }
////                }
////            }
////            dataManager.getAccessTokenAndLogin(TAG, NetConstants.DEFAULT_USERNAME, NetConstants.PASSWORD, null)
////                    .subscribeOn(Schedulers.io())
////                    .observeOn(AndroidSchedulers.mainThread())
////                    .subscribe(d)
////            compositeDisposable.add(d)
////        }
//    }
//
//    override fun onFacebookLoginClick() {
//        mvpView?.apply {
//            showLoading()
//        }
//    }
//
//    companion object {
//
//        private val TAG = "LoginPresenter"
//    }
//}
