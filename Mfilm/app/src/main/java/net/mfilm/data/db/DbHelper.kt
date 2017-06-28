package net.mfilm.data.db

import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.realm.RealmObject
import io.realm.RealmResults
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm

/**
 * Created by tusi on 3/29/17.
 */
interface DbHelper {
    fun loadSearchHistory(observer: DisposableObserver<RealmResults<SearchQueryRealm>>? = null): Disposable
    fun loadFavorites(observer: DisposableObserver<RealmResults<MangaFavoriteRealm>>? = null): Disposable

    fun loadHistory(observer: DisposableObserver<RealmResults<MangaHistoryRealm>>? = null): Disposable
    fun isFavorite(id: Int): MangaFavoriteRealm?
//    fun isHistory(id: Int): MangaHistoryRealm?
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