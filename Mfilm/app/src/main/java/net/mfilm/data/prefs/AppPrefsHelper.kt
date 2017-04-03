package net.mfilm.data.prefs

import android.content.Context
import android.content.SharedPreferences
import net.mfilm.di.AppContext
import net.mfilm.di.PrefsInfo
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by tusi on 4/3/17.
 */
@Singleton
class AppPrefsHelper @Inject constructor(@AppContext context: Context,
                                         @PrefsInfo prefsFileName: String) : PrefsHelper {
    private val mPrefs: SharedPreferences

    init {
        mPrefs = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
    }

}