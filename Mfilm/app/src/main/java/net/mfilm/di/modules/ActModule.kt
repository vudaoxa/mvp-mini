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

package net.mfilm.di.modules

//import net.mfilm.ui.login.LoginPresenter

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import net.mfilm.di.ActContext
import net.mfilm.di.PerActivity
import net.mfilm.ui.about.AboutMvpPresenter
import net.mfilm.ui.about.AboutMvpView
import net.mfilm.ui.about.AboutPresenter
import net.mfilm.ui.home.HomeMVPView
import net.mfilm.ui.home.HomeMvpPresenter
import net.mfilm.ui.home.HomePresenter
import net.mfilm.ui.main.MainMvpPresenter
import net.mfilm.ui.main.MainMvpView
import net.mfilm.ui.main.MainPresenter
import net.mfilm.ui.splash.SplashMvpPresenter
import net.mfilm.ui.splash.SplashMvpView
import net.mfilm.ui.splash.SplashPresenter
import net.mfilm.ui.videos.base.VideoMvpPresenter
import net.mfilm.ui.videos.base.VideoMvpView
import net.mfilm.ui.videos.base.VideoPresenter

/**
 * Created by janisharali on 27/01/17.
 */

@Module
class ActModule(val mActivity: Activity) {

    @Provides
    @ActContext
    fun provideContext(): Context {
        return mActivity
    }

    @Provides
    fun provideActivity(): Activity {
        return mActivity
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @PerActivity
    fun provideSplashPresenter(presenter: SplashPresenter<SplashMvpView>): SplashMvpPresenter<SplashMvpView> {
        return presenter
    }

    @Provides
    fun provideAboutPresenter(presenter: AboutPresenter<AboutMvpView>): AboutMvpPresenter<AboutMvpView> {
        return presenter
    }

//    @Provides
//    @PerActivity
//    fun provideLoginPresenter(presenter: LoginPresenter<LoginMvpView>): LoginMvpPresenter<LoginMvpView> {
//        return presenter
//    }

    @Provides
    @PerActivity
    fun provideMainPresenter(presenter: MainPresenter<MainMvpView>): MainMvpPresenter<MainMvpView> {
        return presenter
    }

    @Provides
    fun provideHomePresenter(presenter: HomePresenter<HomeMVPView>): HomeMvpPresenter<HomeMVPView> {
        return presenter
    }

    @Provides
    @PerActivity
    fun provideVideoMvpPresenter(presenter: VideoPresenter<VideoMvpView>): VideoMvpPresenter<VideoMvpView> {
        return presenter
    }
}
