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

package net.mfilm.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import net.mfilm.BuildConfig
import net.mfilm.R
import net.mfilm.data.AppDataManager
import net.mfilm.data.DataManager
import net.mfilm.data.db.AppDbHelper
import net.mfilm.data.db.DbHelper
import net.mfilm.data.prefs.AppPrefsHelper
import net.mfilm.data.prefs.PrefsHelper
import net.mfilm.utils.AppToast
import net.mfilm.utils.PREF_NAME
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Singleton

/**
 * Created by janisharali on 27/01/17.
 */

@Module
class AppModule(val mApplication: Application) {

    @Provides
    @AppContext
    fun provideContext(): Context {
        return mApplication
    }

    @Provides
    fun provideApplication() = mApplication

//    @Provides
//    @DbInfo
//    fun provideDatabaseName()=DB_NAME

    @Provides
    @ApiInfo
    fun provideApiKey() = BuildConfig.API_KEY

    @Provides
    @PrefsInfo
    fun providePreferenceName() = PREF_NAME

    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @Singleton
    fun provideDbHelper(appDbHelper: AppDbHelper): DbHelper {
        return appDbHelper
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(appPreferencesHelper: AppPrefsHelper): PrefsHelper {
        return appPreferencesHelper
    }

    @Provides
    @Singleton
    fun provideCalligraphyDefaultConfig(): CalligraphyConfig {
        return CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
    }

    @Provides
    @Singleton
    fun provideAppToastConfig(application: Application) = AppToast(application)
}
