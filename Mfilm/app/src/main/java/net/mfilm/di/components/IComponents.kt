package net.mfilm.di.components

import android.app.Application
import android.content.Context
import dagger.Component
import net.mfilm.MApplication
import net.mfilm.data.DataManager
import net.mfilm.data.network_retrofit.RetrofitService
import net.mfilm.di.AppContext
import net.mfilm.di.PerActivity
import net.mfilm.di.modules.ActModule
import net.mfilm.di.modules.AppModule
import net.mfilm.di.modules.NetModule
import net.mfilm.di.scope.UserScope
import net.mfilm.ui.about.AboutFragment
import net.mfilm.ui.categories.CategoriesFragment
import net.mfilm.ui.chapter_images.ChapterImagesFragment
import net.mfilm.ui.chapters.ChaptersFragment
import net.mfilm.ui.favorites.FavoritesFragment
import net.mfilm.ui.history.HistoryFragment
import net.mfilm.ui.main.MainActivity
import net.mfilm.ui.manga_info.MangaInfoFragment
import net.mfilm.ui.mangas.MangasFragment
import net.mfilm.ui.search_history.SearchHistoryFragment
import net.mfilm.ui.settings.SettingsFragment
import net.mfilm.ui.splash.SplashActivity
import net.mfilm.utils.IBus
import javax.inject.Singleton

/**
 * Created by tusi on 3/24/17.
 */

//@PerService
//@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ServiceModule::class))
//interface ServiceComponent {
//    fun inject(service: SyncService)
//}

@UserScope
@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class))
interface AppComponent {
    fun inject(app: MApplication)
    @AppContext
    fun context(): Context

    fun application(): Application
    val dataManager: DataManager
    val iBus: IBus
    val retrofitService: RetrofitService
}

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActModule::class))
interface ActComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)

    fun inject(fragment: AboutFragment)
    fun inject(fragment: MangasFragment)
    fun inject(fragment: SearchHistoryFragment)
    fun inject(fragment: MangaInfoFragment)
    fun inject(fragment: CategoriesFragment)
    fun inject(fragment: ChaptersFragment)
    fun inject(fragment: ChapterImagesFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: SettingsFragment)
}