package net.mfilm.data

import android.content.Context
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.realm.RealmObject
import io.realm.RealmResults
import net.mfilm.data.db.DbHelper
import net.mfilm.data.db.models.MangaRealm
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
    override fun loadSearchHistory(observer: DisposableObserver<RealmResults<SearchQueryRealm>>?): Disposable {
        return mDbHelper.loadSearchHistory(observer)
    }

    override fun loadFavorites(observer: DisposableObserver<RealmResults<MangaRealm>>?): Disposable {
        return mDbHelper.loadFavorites()
    }

    override fun saveObject(obj: RealmObject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveObjects(objs: List<RealmObject>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun realmClose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}