package vn.tieudieu.fragmentstackmanager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by chienchieu on 28/01/2016.
 */
abstract class BaseActivityFragmentStack : BaseActivity(), ScreenManager {

    private var mFragmentStackManager: FragmentStackManager<Fragment>? = null

    override fun initVariables() {}

    override fun initViews(savedInstanceState: Bundle?) {
        initializeFragmentSwapper(savedInstanceState)
    }

    protected abstract val containerView: View
    protected abstract val contentFrameId: Int

    protected abstract val homeClass: Class<*>

    private fun initializeFragmentSwapper(savedInstanceState: Bundle?) {
        val builder = InitializationParams.Builder()
        builder.apply {
            screenManager(this@BaseActivityFragmentStack)
            contentFrame(contentFrameId)
            fragmentManager(supportFragmentManager)
            enableAnimation(true)
            setHomeClass(homeClass)
        }
        mFragmentStackManager = FragmentStackManager()
        mFragmentStackManager?.apply {
            initialize(builder.build())
            onRestoreInstanceState(savedInstanceState)
        }
    }

    override fun onBackPressed() {
        mFragmentStackManager?.popFragment()
    }

    override fun onSwapFragmentRequested(fragment: Fragment) {
        mFragmentStackManager?.swapFragment(fragment)
    }

    override fun onBackFragmentRequested() {
        mFragmentStackManager?.popFragment()
    }

    override fun onCloseRequested() {
        finish()
    }

    protected val fragmentStackManager: FragmentStackManager<Fragment>
        get() {
            mFragmentStackManager?.apply { return this }
            throw IllegalArgumentException()
        }

    protected val currentFragment: Fragment
        get() = mFragmentStackManager?.currentFragment!!
}
