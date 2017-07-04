package vn.tieudieu.fragmentstackmanager

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import java.util.*

/**
 * Created by chienchieu on 27/01/2016.
 */
class FragmentStackManager<F : Fragment> : FragmentStackSwapper<F> {

    private var mInitializationParams: InitializationParams? = null
    private val mUiHandler: Handler = Handler(Looper.getMainLooper())
    private var mContentFragment: F? = null
    private var stackFragments: Stack<F>? = null

    fun initialize(initializationParams: InitializationParams?) {
        if (initializationParams == null) {
            throw IllegalArgumentException("Argument is mandatory")
        }
        mInitializationParams = initializationParams
        stackFragments = Stack<F>()
    }

    private fun performOperationIfAllowed(operation: Runnable) {
        mUiHandler.post { operation.run() }
    }

    private fun performOperationIfAllowed(operation: Runnable, postDelay: Boolean) {
        if (postDelay) {
            mUiHandler.postDelayed({ operation.run() }, TIME_ANIMATION.toLong())
        } else {
            performOperationIfAllowed(operation)
        }
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle?) {

        Log.e(TAG, String.format("onRestoreInstanceState(): savedInstanceState[%b]", savedInstanceState != null))

        if (savedInstanceState == null) {
            mInitializationParams?.screenManager?.onMainScreenRequested()
        } else {
            notifyFragmentChange()
        }
    }

    private fun notifyFragmentChange() {
        if (findCurrentFragment()) {
            mUiHandler.post {
                mInitializationParams?.screenManager?.onFragmentEntered(mContentFragment)
            }
        }
    }

    private fun notifyPause() {
        if (stackFragments!!.size > 0) {
            stackFragments!!.lastElement().onPause()
        }
    }

    private fun notifyCloseRequest() {
        Log.e(TAG, "notifyCloseRequest()")
        mUiHandler.post {
            mInitializationParams?.screenManager?.run {
                onCloseRequested()
            }
        }
    }

    private fun findCurrentFragment(): Boolean {
        // Timber.e("xyz~stackFragment~size"+(stackFragments == null?0:stackFragments.size()));
        if (stackFragments == null || stackFragments!!.size == 0) {
            return false
        } else {
            mContentFragment = stackFragments!!.lastElement()
            return mContentFragment != null
        }
    }

    override fun size(): Int {
        return if (stackFragments == null) 0 else stackFragments!!.size
    }

    override val currentFragment: F
        get() = mContentFragment!!

    override fun getFragmentByTag(tag: String): F? {
        return null
    }

    override fun swapFragment(fragment: F, transparent: Boolean) {
        val operation = Runnable {
            Log.e(TAG, "swapFragment()")
            val ft = mInitializationParams!!.fragmentManager.beginTransaction()
            if (mInitializationParams!!.isAnimationEnabled) {
                ft.setCustomAnimations(R.anim.slide_left_in, 0, 0, 0)
            }
            ft.add(mInitializationParams!!.contentFrame, fragment)
            if (stackFragments!!.size > 0) {
                notifyPause()
                if (!mInitializationParams!!.isAnimationEnabled) {
                    ft.hide(stackFragments!!.lastElement())
                }
            }
            stackFragments!!.push(fragment)
            ft.commit()
            mInitializationParams!!.fragmentManager.executePendingTransactions()
            findCurrentFragment()
            notifyFragmentChange()
        }
        performOperationIfAllowed(operation)

        // hide old fragment if animation anable
        if (mInitializationParams!!.isAnimationEnabled && !transparent) {
            val operationHide = Runnable {
                val ft = mInitializationParams!!.fragmentManager.beginTransaction()
                if (stackFragments!!.size > 1) {
                    ft.hide(stackFragments!![stackFragments!!.size - 2])
                }
                ft.commit()
            }
            performOperationIfAllowed(operationHide, true)
        }
    }

    override fun popFragment() {
        val operation = Runnable {
            val stackEntries = stackFragments!!.size
            // Timber.v(String.format("popFragment(): entries[%d]", stackEntries));
            if (stackEntries >= 2) {
                val ft = mInitializationParams!!.fragmentManager.beginTransaction()
                if (mInitializationParams!!.isAnimationEnabled) {
                    ft.setCustomAnimations(0, R.anim.slide_right_out, 0, 0)
                }
                stackFragments!!.lastElement().onPause()
                ft.remove(stackFragments!!.pop())
                stackFragments!!.lastElement().onResume()
                ft.show(stackFragments!!.lastElement())
                ft.commit()
                findCurrentFragment()
                notifyFragmentChange()
                // Timber.v("popFragment-fragmentStack.size()=" + stackFragments.size());
            } else {
                notifyCloseRequest()
            }
        }
        performOperationIfAllowed(operation)
    }

    override fun clearStack() {
        val operation = Runnable {
            mInitializationParams!!.fragmentManager.executePendingTransactions()
            val ft = mInitializationParams!!.fragmentManager.beginTransaction()

            for (i in stackFragments!!.indices.reversed()) {
                if (stackFragments!![i].javaClass == mInitializationParams!!.homeClass) {
//                    continue
                }
                ft.remove(stackFragments!![i])
                stackFragments!!.removeAt(i)
            }
            ft.commit()
        }
        performOperationIfAllowed(operation)
    }

    override fun clearStackAll() {
        val operation = Runnable {
            mInitializationParams!!.fragmentManager.executePendingTransactions()
            val ft = mInitializationParams!!.fragmentManager.beginTransaction()

            for (i in stackFragments!!.indices.reversed()) {
                ft.remove(stackFragments!![i])
                stackFragments!!.removeAt(i)
            }
            ft.commit()
        }
        performOperationIfAllowed(operation)
    }

    companion object {

        val TIME_ANIMATION = 400
        private val TAG = "FragmentStackManager"
    }
}
