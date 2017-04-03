package net.mfilm

import android.app.Application
import android.support.annotation.StringRes
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util
import net.mfilm.di.AppModule
import net.mfilm.di.components.AppComponent
import net.mfilm.di.components.DaggerAppComponent
import net.mfilm.di.module.NetModule
import net.mfilm.utils.AppConstants
import net.mfilm.utils.AppToast
import net.mfilm.utils.WITH_EXT
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

        CalligraphyConfig.initDefault(mCalligraphyConfig)
        userAgent = Util.getUserAgent(this, "VLOT")
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

    fun buildDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter?): DataSource.Factory {
        return DefaultDataSourceFactory(this, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter))
    }

    fun buildHttpDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter?): HttpDataSource.Factory {
        return DefaultHttpDataSourceFactory(userAgent, bandwidthMeter)
    }

    companion object {
        lateinit var instance: MApplication
    }
}
