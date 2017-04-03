package net.mfilm.di.components

//import net.mfilm.ui.login.LoginActivity
//import net.mfilm.ui.login.LoginFragment
import android.app.Application
import android.content.Context
import dagger.Component
import net.mfilm.MApplication
import net.mfilm.data.DataMng
import net.mfilm.di.AppContext
import net.mfilm.di.AppModule
import net.mfilm.di.PerActivity
import net.mfilm.di.module.NetModule
import net.mfilm.di.modules.ActModule
import net.mfilm.di.scope.UserScope
import net.mfilm.ui.about.AboutFragment
import net.mfilm.ui.home.HomeFragment
import net.mfilm.ui.main.MainActivity
import net.mfilm.ui.splash.SplashActivity
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
    //    fun inject(service: SyncService)
    @AppContext
    fun context(): Context

    fun application(): Application
    val dataManager: DataMng
}

@PerActivity
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ActModule::class))
interface ActComponent {
    fun inject(activity: MainActivity)
    //    fun inject(activity: LoginActivity)
    fun inject(activity: SplashActivity)

    fun inject(fragment: AboutFragment)
    fun inject(fragment: HomeFragment)
//    fun inject(fragment: LoginFragment)
}