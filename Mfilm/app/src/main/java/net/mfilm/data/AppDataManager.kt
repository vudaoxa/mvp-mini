package net.mfilm.data

import android.content.Context
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults
import net.mfilm.data.db.DbHelper
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.data.prefs.PrefsHelper
import net.mfilm.di.AppContext
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class AppDataManager @Inject constructor(@AppContext val mContext: Context, val mDbHelper: DbHelper,
                                         val mPrefsHelper: PrefsHelper, val mRetrofitService: RetrofitService) : DataManager {
    override var mangaSourceIndex: Int
        get() = mPrefsHelper.mangaSourceIndex
        set(value) {
            mPrefsHelper.mangaSourceIndex = value
        }
    override var historyEnabled: Boolean
        get() = mPrefsHelper.historyEnabled
        set(value) {
            mPrefsHelper.historyEnabled = value
        }
    override var searchHistoryEnabled: Boolean
        get() = mPrefsHelper.searchHistoryEnabled
        set(value) {
            mPrefsHelper.searchHistoryEnabled = value
        }
    override fun loadSearchHistory(observer: DisposableObserver<RealmResults<SearchQueryRealm>>?): Disposable {
        return mDbHelper.loadSearchHistory(observer)
    }

    override fun isFavorite(id: Int): MangaFavoriteRealm? {
        return mDbHelper.isFavorite(id)
    }

    override fun loadFavorites(observer: DisposableObserver<RealmResults<MangaFavoriteRealm>>?): Disposable {
        return mDbHelper.loadFavorites(observer)
    }

    override fun loadHistory(observer: DisposableObserver<RealmResults<MangaHistoryRealm>>?): Disposable {
        return mDbHelper.loadHistory(observer)
    }
    override fun saveObject(obj: RealmObject) {
        mDbHelper.saveObject(obj)
    }

    override fun saveObjects(objs: List<RealmObject>) {
        mDbHelper.saveObjects(objs)
    }

    override fun delete(clazz: Class<out RealmModel>) {
        mDbHelper.delete(clazz)
    }
    override fun realmClose() {
        mDbHelper.realmClose()
    }

}