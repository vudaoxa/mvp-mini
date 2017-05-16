package net.mfilm

import android.app.Application
import android.support.annotation.StringRes
import net.mfilm.di.AppModule
import net.mfilm.di.components.AppComponent
import net.mfilm.di.components.DaggerAppComponent
import net.mfilm.di.module.NetModule
import net.mfilm.utils.*
import org.jetbrains.annotations.NotNull
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
        TimeUtils
        CalligraphyConfig.initDefault(mCalligraphyConfig)
        instance = this
    }

    fun showMessage(@AppConstants.TypeToast typeToast: Int, @NotNull message: String) {
        mAppToast.showMessageByType(typeToast, message)

    }

    fun showMessage(@AppConstants.TypeToast typeToast: Int, @StringRes resIdString: Int) {
        val message = mAppComponent.application().resources.getString(resIdString)
        mAppToast.showMessageByType(typeToast, message)
    }

    fun useExtensionRenderers(): Boolean {
        return BuildConfig.FLAVOR == WITH_EXT
    }


    companion object {
        lateinit var instance: MApplication
    }
}
