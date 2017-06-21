package net.mfilm.data.db

import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.realm.RealmObject
import io.realm.RealmResults
import net.mfilm.data.db.models.MangaRealm
import net.mfilm.data.db.models.SearchQueryRealm

/**
 * Created by tusi on 3/29/17.
 */
interface DbHelper {
    //    fun <V : RealmObject> find(results: RealmResults<V>): Flowable<RealmResults<V>>
    fun loadSearchHistory(observer: DisposableObserver<RealmResults<SearchQueryRealm>>? = null): Disposable
    fun loadFavorites(observer: DisposableObserver<RealmResults<MangaRealm>>? = null): Disposable
//    fun loadViewHistory()
//    fun updateSearchHistoryRemoval()
//    fun updateSearchesHistoryRemoval()
//    fun updateFavoriteRemoval()
//    fun updateFavoritesRemoval()
//    fun updateViewHistoryRemoval()
//    fun updateViewsHistoryRemoval()

    fun saveObject(obj: RealmObject)
    fun saveObjects(objs: List<RealmObject>)
    fun realmClose()
}