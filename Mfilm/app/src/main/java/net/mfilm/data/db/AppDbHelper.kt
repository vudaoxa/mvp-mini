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
import net.mfilm.data.db.models.ChapterRealm
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.data.network_retrofit.Manga
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
class AppDbHelper @Inject constructor() : DbHelper {
    private fun <V : RealmObject> toFlowable(results: RealmResults<V>): Flowable<RealmResults<V>> {
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

    private fun <V : RealmObject> toFlowable(results: RealmList<V>): Flowable<RealmList<V>> {
        return Flowable.create(FlowableOnSubscribe<RealmList<V>> { emitter ->
            val realm = Realm.getDefaultInstance()

            val listener = RealmChangeListener<RealmList<V>> {}
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
        return toFlowable<SearchQueryRealm>(realm.where(SearchQueryRealm::class.java)
                .equalTo("status", true)
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

    override fun loadFavorites(observer: DisposableObserver<RealmResults<MangaFavoriteRealm>>?): Disposable {
        val realm = Realm.getDefaultInstance()
        return toFlowable<MangaFavoriteRealm>(realm.where(MangaFavoriteRealm::class.java).equalTo("favorite", true)
                .findAll())
                .subscribe({
                    Timber.e("loadFavorites----------isLoaded")
                    observer?.onNext(it)
                }, {
                    it.printStackTrace()
                    realm.close()
                })
    }

    override fun loadHistory(observer: DisposableObserver<RealmResults<MangaHistoryRealm>>?): Disposable {
        val realm = Realm.getDefaultInstance()
        return toFlowable<MangaHistoryRealm>(realm.where(MangaHistoryRealm::class.java).equalTo("history", true)
                .findAll())
                .subscribe({
                    Timber.e("loadFavorites----------isLoaded")
                    observer?.onNext(it)
                }, {
                    it.printStackTrace()
                    realm.close()
                })
    }

    override fun isFavorite(id: Int): MangaFavoriteRealm? {
        val realm = Realm.getDefaultInstance()
        val item = realm.where(MangaFavoriteRealm::class.java).equalTo("id", id).equalTo("favorite", true).findFirst()
        return item
    }

    override fun requestMangaHistory(id: Int): MangaHistoryRealm? {
        val realm = Realm.getDefaultInstance()
        val item = realm.where(MangaHistoryRealm::class.java).equalTo("id", id).equalTo("history", true).findFirst()
        return item
    }

    override fun requestChaptersHistory(id: Int, observer: DisposableObserver<RealmList<ChapterRealm>>?): Disposable? {
        val realm = Realm.getDefaultInstance()
        val item = realm.where(MangaHistoryRealm::class.java).equalTo("id", id)
//                .equalTo("history", true)
                .findFirst()
        item?.run {
            readChaptersIds.run {
                Timber.e("-requestChaptersHistory---readChaptersIds------------$this---------------------------")
                return toFlowable<ChapterRealm>(this)
                        .subscribe({
                            Timber.e("requestChaptersHistory----------isLoaded")
                            observer?.onNext(it)
                        }, {
                            it.printStackTrace()
                            realm.close()
                        })

            }
        }
        return null
    }

    override fun saveChapterHistory(chapter: Chapter) {
        chapter.let { c ->
            c.run {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    val item = realm.where(MangaHistoryRealm::class.java).equalTo("id", mangaId)
//                            .equalTo("history", true)
                            .findFirst()
                    item.let { it ->
                        it?.run {
                            val id = c.id!!
                            it.readChaptersIds.run {
                                Timber.e("-saveChapterHistory---readChaptersIds------------$this---------------------------")
                                if (!map { it.id }.contains(id)) {
                                    add(ChapterRealm(id))
                                }
                            }
                            it.readingChapterId = id
//                            it.readingChapterPosition = position
                            it.time = System.currentTimeMillis()
                        }
                    }
                }
            }
        }
    }

    //not use
    override fun saveReadingChapter(chapter: Chapter) {
        chapter.let { c ->
            c.run {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    val item = realm.where(MangaHistoryRealm::class.java).equalTo("id", mangaId)
//                            .equalTo("history", true)
                            .findFirst()
                    item.let { it ->
                        it?.run {
                            it.readingChapterId = c.id!!
//                            it.readingChapterPosition = position
                        }
                    }
                }
            }
        }
    }

    override fun saveReadingPage(chapter: Chapter, page: Int) {
        chapter.let { c ->
            c.run {
                val realm = Realm.getDefaultInstance()
                realm.executeTransaction {
                    val item = realm.where(MangaHistoryRealm::class.java).equalTo("id", mangaId).equalTo("history", true).findFirst()
                    item.let { it ->
                        it?.run {
                            it.readingPage = page
                        }
                    }
                }
            }
        }
    }

    override fun saveMangaHistory(manga: Manga) {
        val currentTime = System.currentTimeMillis()
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val item = realm.where(MangaHistoryRealm::class.java).equalTo("id", manga.id).equalTo("history", true).findFirst()
            item.let { it ->
                it?.run {
                    it.time = currentTime
                } ?: let {
                    manga.run {
                        realm.insert(MangaHistoryRealm(id, name, coverUrl, currentTime, true))
                    }
                }
            }
        }
    }
    override fun saveObject(obj: RealmObject) {
        Timber.e("--------saveObject------ $obj------")
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.insertOrUpdate(obj) }
    }

    override fun saveObjects(objs: List<RealmObject>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.insertOrUpdate(objs) }
    }

    override fun delete(clazz: Class<out RealmModel>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.delete(clazz) }
    }

    override fun realmClose() {
        Realm.getDefaultInstance().close()
    }
}
