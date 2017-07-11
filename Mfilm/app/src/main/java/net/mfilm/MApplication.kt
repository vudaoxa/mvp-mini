package net.mfilm

import android.support.annotation.StringRes
import android.support.multidex.MultiDexApplication
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.fresco.FrescoImageLoader
import io.realm.Realm
import io.realm.RealmConfiguration
import net.mfilm.di.components.AppComponent
import net.mfilm.di.components.DaggerAppComponent
import net.mfilm.di.modules.AppModule
import net.mfilm.di.modules.NetModule
import net.mfilm.google.initAds
import net.mfilm.google.initTracking
import net.mfilm.utils.*
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import timber.log.Timber.DebugTree
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject

/**
 * Created by Dieu on 02/03/2017.
 */

class MApplication : MultiDexApplication() {
    @Inject
    lateinit var mCalligraphyConfig: CalligraphyConfig

    @Inject
    lateinit var mAppToast: AppToast

    // Needed to replace the component with a test specific one
    lateinit var mAppComponent: AppComponent
    lateinit var userAgent: String

    override fun onCreate() {
        super.onCreate()
        mAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule("xxxx"))
                .build()

        mAppComponent.inject(this)
        initAwesome()
        initAnimations(this)
        initIcons(this)
        initNavs()
        initFresco()
        initTimber()
        initRealm()
        TimeUtils
        initTracking(this)
        initAds(this)
        CalligraphyConfig.initDefault(mCalligraphyConfig)
        instance = this
    }

    fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    fun initFresco() {
//        val config = ImagePipelineConfig.newBuilder(this)
//                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
//                .setResizeAndRotateEnabledForNetwork(true)
//                .setDownsampleEnabled(true)
//                .build()
//        Fresco.initialize(this, config)
        BigImageViewer.initialize(FrescoImageLoader.with(this));

    }
    fun showMessage(@AppConstants.TypeToast typeToast: Int, @NotNull message: String) {
        mAppToast.showMessageByType(typeToast, message)
    }

    fun showMessage(@AppConstants.TypeToast typeToast: Int, @StringRes resIdString: Int) {
        mAppToast.showMessageByType(typeToast, resIdString)
    }

    fun showMessage(@AppConstants.TypeToast typeToast: Int, @NotNull message: Any?) {
        when (message) {
            is Int -> {
                showMessage(typeToast, message)
            }
            is String -> {
                showMessage(typeToast, message)
            }
        }
    }
    fun useExtensionRenderers(): Boolean {
        return BuildConfig.FLAVOR == WITH_EXT
    }

    fun initRealm() {
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("komik.realm")
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
    companion object {
        lateinit var instance: MApplication
    }
}
