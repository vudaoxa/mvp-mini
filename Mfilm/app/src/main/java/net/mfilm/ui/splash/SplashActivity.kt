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

//import net.mfilm.ui.login.LoginActivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import net.mfilm.R
import net.mfilm.ui.base.BaseActivity
import net.mfilm.ui.main.MainActivity
import net.mfilm.ui.manga.PassByTime
import javax.inject.Inject


/**
 * Created by janisharali on 27/01/17.
 */

class SplashActivity : BaseActivity(), SplashMvpView {

    //not use
    override var screenRequestPassByTime: PassByTime?
        get() = null
        set(value) {}

    override fun initScreenRequestPassByTime() {

    }

    override fun onNoInternetConnections() {
    }

    override fun onFailure() {
    }

    @Inject
    lateinit var mSplashPresenter: SplashMvpPresenter<SplashMvpView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mActComponent.inject(this)
        mSplashPresenter.onAttach(this)
    }

    /**
     * Making the screen wait so that the  branding can be shown
     */
    override fun openLoginActivity() {
//        val intent = LoginActivity.getStartIntent(this)
//        startActivity(intent)
//        finish()
    }

    override fun openMainActivity() {
        val intent = MainActivity.getStartIntent(this)
        startActivity(intent)
        finish()
    }

    override fun startSyncService() {
        //        SyncService.start(this);
    }

    override fun onDestroy() {
        mSplashPresenter.onDetach()
        super.onDestroy()
    }

    override fun setUp() {

    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, SplashActivity::class.java)
            return intent
        }
    }
}
