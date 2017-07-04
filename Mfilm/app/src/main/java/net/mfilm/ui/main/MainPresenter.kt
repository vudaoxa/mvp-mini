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


import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable
import net.mfilm.data.DataManager
import net.mfilm.ui.base.BasePresenter
import net.mfilm.utils.Favorite
import net.mfilm.utils.IBus
import net.mfilm.utils.TapEvent
import javax.inject.Inject


/**
 * Created by janisharali on 27/01/17.
 */

class MainPresenter<V : MainMvpView> @Inject
constructor(val iBus: IBus, dataManager: DataManager, compositeDisposable: CompositeDisposable)
    : BasePresenter<V>(dataManager, compositeDisposable), MainMvpPresenter<V> {

    override fun onAttach(mvpView: V) {
        super.onAttach(mvpView)
        initIBus()
    }

    override fun initIBus() {
        //flowable to receive favorite response from mangaInfoPresenter to up date mvpView
        val flowable = iBus.asFlowable().filter { it is Favorite }
        val favEventEmitter = flowable.publish()
        compositeDisposable.run {
            add(favEventEmitter.subscribe { mvpView?.isFavorite((it as? Favorite?)?.fav) })
            add(favEventEmitter.connect())
        }
    }

    override fun sendOptionsMenuItem(item: MenuItem) {
        iBus.send(item)
    }
    override fun onFollow() {
        iBus.send(TapEvent)
    }
    override fun onDrawerOptionAboutClick() {
        mvpView!!.showAboutFragment()
    }

    override fun onDrawerOptionLogoutClick() {
        mvpView!!.showLoading()

    }

    override fun onViewInitialized() {

    }

    override fun onCardExhausted() {

    }

    override fun onNavMenuCreated() {
        mvpView?.run {
            updateAppVersion()
        }
    }
}
