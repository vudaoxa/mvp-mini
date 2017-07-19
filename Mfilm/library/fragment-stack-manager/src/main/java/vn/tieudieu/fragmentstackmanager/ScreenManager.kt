package vn.tieudieu.fragmentstackmanager

import android.support.v4.app.Fragment

/**
 * Created by chienchieu on 27/01/2016.
 */
interface ScreenManager {
    fun onMainScreenRequested()
    fun onSwapFragmentRequested(fragment: Fragment)
    fun onBackFragmentRequested()
    fun onFragmentEntered(f: Fragment?)
    fun onCloseRequested()
    fun onNewScreenRequested(indexTag: Any?, typeContent: String? = null, obj: Any? = null)
//    fun onNewFragmentRequested(indexTag: Any?, fragment: Fragment? = null, obj: Any? = null)
}
