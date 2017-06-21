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

package net.mfilm.data.db


import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableObserver
import io.realm.*
import net.mfilm.data.db.models.MangaRealm
import net.mfilm.data.db.models.SearchQueryRealm
import timber.log.Timber
import javax.inject.Singleton


/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
class AppDbHelper : DbHelper {
    private fun <V : RealmObject> find(results: RealmResults<V>): Flowable<RealmResults<V>> {
        return Flowable.create(FlowableOnSubscribe<RealmResults<V>> { emitter ->
            val realm = Realm.getDefaultInstance()

            val listener = RealmChangeListener<RealmResults<V>> {
                //                if (!emitter.isCancelled) {
//                    emitter.onNext(results)
//                }
            }
            emitter.setDisposable(Disposables.fromRunnable {
                results.removeChangeListener(listener)
                realm.close()
            })
            results.addChangeListener(listener)
            emitter.onNext(results)
        }, BackpressureStrategy.LATEST)
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
    }

    override fun loadSearchHistory(observer: DisposableObserver<RealmResults<SearchQueryRealm>>?): Disposable {
        val realm = Realm.getDefaultInstance()
        //it will be added to presenter, and will be cleared on onDetach
        return find<SearchQueryRealm>(realm.where(SearchQueryRealm::class.java)
                .findAllSortedAsync("time", Sort.DESCENDING))
                .subscribe({
                    Timber.e("loadSearchHistory------------isLoaded")
                    observer?.onNext(it)
                }, {
                    it.printStackTrace()
                    realm.close()
                }, {
                    realm.close()
                })
    }

    override fun loadFavorites(observer: DisposableObserver<RealmResults<MangaRealm>>?): Disposable {
        val realm = Realm.getDefaultInstance()
        return find<MangaRealm>(realm.where(MangaRealm::class.java).equalTo("fav", 1)
                .findAll())
                .subscribe({
                    Timber.e("loadFavorites----------isLoaded")
                    observer?.onNext(it)
                }, {
                    it.printStackTrace()
                    realm.close()
                }, {
                    //                    realm.close()
                })
    }

    override fun saveObject(obj: RealmObject) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(obj)
        realm.commitTransaction()
    }

    override fun saveObjects(objs: List<RealmObject>) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(objs)
        realm.commitTransaction()
    }

    override fun realmClose() {
        Realm.getDefaultInstance().close()
    }
}
