package net.mfilm.data

import net.mfilm.data.db.DbHelper
import net.mfilm.data.network_retrofit.ApisRetrofit
import net.mfilm.data.prefs.PrefsHelper

/**
 * Created by tusi on 3/29/17.
 */
interface DataManager : DbHelper, PrefsHelper, ApisRetrofit {

}