package net.mfilm.data

import android.content.Context
import io.reactivex.Observable
import net.mfilm.data.db.DbHelper
import net.mfilm.data.networkretrofit.RetrofitService
import net.mfilm.data.prefs.PrefsHelper
import net.mfilm.di.AppContext
import javax.inject.Inject

/**
 * Created by tusi on 4/2/17.
 */
class AppDataMng @Inject constructor(@AppContext val mContext: Context, val mDbHelper: DbHelper,
                                     val mPrefsHelper: PrefsHelper, val mRetrofitService: RetrofitService) : DataMng {
    override fun seedDatabaseQuestions(): Observable<Boolean> {
        return Observable.just(true)
    }

    override fun seedDatabaseOptions(): Observable<Boolean> {
        return Observable.just(true)
    }

}