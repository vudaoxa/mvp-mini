package vn.tieudieu.fragmentstackmanager

import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * Created by chienchieu on 28/01/2016.
 */
abstract class BaseActivityFragmentStack : BaseActivity(), ScreenManager {

    private var mFragmentStackManager: FragmentStackManager<Fragment>? = null

    override fun initVariables() {}

    override fun initViews(savedInstanceState: Bundle?) {
        initializeFragmentSwapper(savedInstanceState)
    }

    protected abstract val contentFrameId: Int

    protected abstract val homeClass: Class<*>

    private fun initializeFragmentSwapper(savedInstanceState: Bundle?) {
        val builder = InitializationParams.Builder()
        builder.screenManager(this)
        builder.contentFrame(contentFrameId)
        builder.fragmentManager(supportFragmentManager)
        builder.enableAnimation(false)
        builder.setHomeClass(homeClass)

        mFragmentStackManager = FragmentStackManager()
        mFragmentStackManager!!.initialize(builder.build())
        mFragmentStackManager!!.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        mFragmentStackManager!!.popFragment()
    }

    override fun onSwapFragmentRequested(fragment: Fragment) {
        mFragmentStackManager!!.swapFragment(fragment)
    }

    override fun onBackFragmentRequested() {
        mFragmentStackManager!!.popFragment()
    }

    override fun onCloseRequested() {
        finish()
    }

    protected val fragmentStackManager: FragmentStackManager<Fragment>
        get() {
            if (mFragmentStackManager == null) {
                throw IllegalArgumentException()
            }
            return mFragmentStackManager!!
        }

    protected val currentFragment: Fragment
        get() = mFragmentStackManager!!.currentFragment
}
