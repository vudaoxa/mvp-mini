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
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import net.mfilm.R
//import net.mfilm.ui.base.BaseActivity
//import net.mfilm.ui.main.MainActivity
//import kotlinx.android.synthetic.main.activity_login.*
//import javax.inject.Inject
//
//
///**
// * Created by janisharali on 27/01/17.
// */
//
//class LoginActivity : BaseActivity(), LoginMvpView {
//
//    @Inject
//    lateinit var mPresenter: LoginMvpPresenter<LoginMvpView>
//
////    @BindView(R.id.et_email)
////    internal var et_email: EditText? = null
////
////    @BindView(R.id.et_password)
////    internal var et_password: EditText? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        mActComponent.inject(this)
//
//        mPresenter.onAttach(this)
//    }
//
//
//    @OnClick(R.id.btn_server_login)
//    internal fun onServerLoginClick(v: View) {
//        mPresenter.onServerLoginClick(et_email.text.toString(),
//                et_password.text.toString())
//    }
//
//    @OnClick(R.id.ib_google_login)
//    internal fun onGoogleLoginClick(v: View) {
//        mPresenter.onGoogleLoginClick()
//    }
//
//    @OnClick(R.id.ib_fb_login)
//    internal fun onFbLoginClick(v: View) {
//        mPresenter.onFacebookLoginClick()
//    }
//
//
//    override fun openMainActivity() {
//        //        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
//        val intent = MainActivity.getStartIntent(this@LoginActivity)
//        startActivity(intent)
//        finish()
//    }
//
//
//    override fun showLoading() {}
//
//    override fun hideLoading() {}
//
//    override fun onDestroy() {
//        mPresenter.onDetach()
//        super.onDestroy()
//    }
//
//    override fun setUp() {
//
//    }
//
//    companion object {
//
//        fun getStartIntent(context: Context): Intent {
//            val intent = Intent(context, LoginActivity::class.java)
//            return intent
//        }
//    }
//}
