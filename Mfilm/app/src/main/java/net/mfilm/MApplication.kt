package net.mfilm

import android.app.Application
import android.support.annotation.StringRes
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import io.realm.Realm
import io.realm.RealmConfiguration
import net.mfilm.di.AppModule
import net.mfilm.di.components.AppComponent
import net.mfilm.di.module.NetModule
import net.mfilm.utils.*
import org.jetbrains.annotations.NotNull
import timber.log.Timber
import timber.log.Timber.DebugTree
import uk.co.chrisjenx.calligraphy.CalligraphyConfig
import javax.inject.Inject


/**
 * Created by Dieu on 02/03/2017.
 */

class MApplication : Application() {
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
        CalligraphyConfig.initDefault(mCalligraphyConfig)
        instance = this
    }

    fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    fun initFresco() {
        val config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
                .setResizeAndRotateEnabledForNetwork(true)
                .setDownsampleEnabled(true)
                .build()
        Fresco.initialize(this, config)
    }
    fun showMessage(@AppConstants.TypeToast typeToast: Int, @NotNull message: String) {
        mAppToast.showMessageByType(typeToast, message)

    }

    fun showMessage(@AppConstants.TypeToast typeToast: Int, @StringRes resIdString: Int) {
        mAppToast.showMessageByType(typeToast, resIdString)
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
