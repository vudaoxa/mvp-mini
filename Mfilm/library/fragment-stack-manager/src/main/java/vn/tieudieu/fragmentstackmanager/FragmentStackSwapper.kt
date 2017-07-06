package vn.tieudieu.fragmentstackmanager

import android.support.v4.app.Fragment

/**
 * Created by chienchieu on 27/01/2016.
 */
interface FragmentStackSwapper<F : Fragment> {
    fun swapFragment(fragment: F, transparent: Boolean = false)
    fun popFragment()
    fun clearStack()
    fun clearStackAll()
    fun size(): Int
    val currentFragment: F?
    fun getFragmentByTag(tag: String): F?
}
