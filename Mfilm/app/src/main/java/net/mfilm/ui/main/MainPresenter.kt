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

package net.mfilm.ui.main


import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataMng
import net.mfilm.ui.base.BasePresenter
import javax.inject.Inject


/**
 * Created by janisharali on 27/01/17.
 */

class MainPresenter<V : MainMvpView> @Inject
constructor(dataManager: DataMng, compositeDisposable: CompositeDisposable) : BasePresenter<V>(dataManager, compositeDisposable), MainMvpPresenter<V> {

    override fun onDrawerOptionAboutClick() {
        mvpView!!.showAboutFragment()
    }

    override fun onDrawerOptionLogoutClick() {
        mvpView!!.showLoading()
        //
        //        getCompositeDisposable().add(getDataManager().doLogoutApiCall()
        //                .subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Consumer<LogoutResponse>() {
        //                    @Override
        //                    public void accept(LogoutResponse response) throws Exception {
        //                        if(!isViewAttached()) {
        //                            return;
        //                        }
        //
        //                        getDataManager().setUserAsLoggedOut();
        //                        getMvpView().hideLoading();
        //                        getMvpView().openLoginActivity();
        //                    }
        //                }, new Consumer<Throwable>() {
        //                    @Override
        //                    public void accept(Throwable throwable) throws Exception {
        //                        if(!isViewAttached()) {
        //                            return;
        //                        }
        //
        //                        getMvpView().hideLoading();
        //
        //                        // handle the login error here
        //                        if (throwable instanceof ANError) {
        //                            ANError anError = (ANError) throwable;
        //                            handleApiError(anError);
        //                        }
        //                    }
        //                }));

    }

    override fun onViewInitialized() {

    }

    override fun onCardExhausted() {

    }

    override fun onNavMenuCreated() {
        if (!isViewAttached) {
            return
        }
        mvpView!!.updateAppVersion()

//        val currentUserName = dataManager.currentUserName
//        if (currentUserName != null && !currentUserName.isEmpty()) {
//            mvpView!!.updateUserName(currentUserName)
//        }
//
//        val currentUserEmail = dataManager.currentUserEmail
//        if (currentUserEmail != null && !currentUserEmail.isEmpty()) {
//            mvpView!!.updateUserEmail(currentUserEmail)
//        }
//
//        val profilePicUrl = dataManager.currentUserProfilePicUrl
//        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
//            mvpView!!.updateUserProfilePic(profilePicUrl)
//        }
    }

    companion object {

//        private val TAG = MainPresenter<*>::class.java.simpleName
    }
}
