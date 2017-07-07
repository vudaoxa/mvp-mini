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
    companion object {
        private const val PREF_KEY_MANGA_SOURCE_INDEX = "PREF_KEY_MANGA_SOURCE_INDEX"
        private const val PREF_KEY_HISTORY_ENABLED = "PREF_KEY_HISTORY_ENABLED"
        private const val PREF_KEY_SEARCH_HISTORY_ENABLED = "PREF_KEY_SEARCH_HISTORY_ENABLED"
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)
    override var mangaSourceIndex: Int
        get() = mPrefs.getInt(PREF_KEY_MANGA_SOURCE_INDEX, 0)
        set(value) {
            mPrefs.edit().putInt(PREF_KEY_MANGA_SOURCE_INDEX, value).apply()
        }
    override var historyEnabled: Boolean
        get() = mPrefs.getBoolean(PREF_KEY_HISTORY_ENABLED, true)
        set(value) {
            mPrefs.edit().putBoolean(PREF_KEY_HISTORY_ENABLED, value).apply()
        }
    override var searchHistoryEnabled: Boolean
        get() = mPrefs.getBoolean(PREF_KEY_SEARCH_HISTORY_ENABLED, true)
        set(value) {
            mPrefs.edit().putBoolean(PREF_KEY_SEARCH_HISTORY_ENABLED, value).apply()
        }
}