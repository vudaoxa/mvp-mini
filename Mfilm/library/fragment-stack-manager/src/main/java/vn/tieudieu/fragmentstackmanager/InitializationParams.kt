package vn.tieudieu.fragmentstackmanager

import android.support.v4.app.FragmentManager

/**
 * Created by chienchieu on 27/01/2016.
 */
class InitializationParams private constructor(builder: InitializationParams.Builder) {

    val fragmentManager: FragmentManager
    val contentFrame: Int
    val screenManager: ScreenManager
    val isAnimationEnabled: Boolean
    val homeClass: Class<*>

    init {
        fragmentManager = builder.mFragmentManager!!
        contentFrame = builder.mContentFrame!!
        screenManager = builder.mScreenManager!!
        isAnimationEnabled = builder.mIsAnimationEnabled
        homeClass = builder.mHomeClass!!
    }

    /**
     * Parameters builder class.
     */
    class Builder {
        var mFragmentManager: FragmentManager? = null
        var mContentFrame: Int? = null
        var mScreenManager: ScreenManager? = null
        var mIsAnimationEnabled = false
        var mHomeClass: Class<*>? = null

        fun fragmentManager(fragmentManager: FragmentManager): Builder {
            mFragmentManager = fragmentManager
            return this
        }

        fun contentFrame(contentFrame: Int): Builder {
            mContentFrame = contentFrame
            return this
        }

        fun screenManager(screenManager: ScreenManager): Builder {
            mScreenManager = screenManager
            return this
        }

        fun enableAnimation(enable: Boolean): Builder {
            mIsAnimationEnabled = enable
            return this
        }

        fun setHomeClass(cls: Class<*>): Builder {
            mHomeClass = cls
            return this
        }

        fun build(): InitializationParams {
            if (mFragmentManager == null || mContentFrame == null || mScreenManager == null) {
                throw IllegalStateException("All parameters are mandatory")
            }
            return InitializationParams(this)
        }
    }
}
