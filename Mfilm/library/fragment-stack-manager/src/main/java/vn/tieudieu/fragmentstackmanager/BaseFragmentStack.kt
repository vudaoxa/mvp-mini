package vn.tieudieu.fragmentstackmanager

import android.content.Context
import java.lang.IllegalArgumentException

/**
 * Created by chienchieu on 27/01/2016.
 */
abstract class BaseFragmentStack : BaseFragment() {
    var title: String? = null
    var back = false
    protected var screenManager: ScreenManager? = null
        private set

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ScreenManager) {
            screenManager = context as ScreenManager?
        } else {
            throw IllegalArgumentException()
        }
    }

    fun getFragmentTitle(): String? {
        return title
    }

    fun showBackButton(): Boolean {
        return back
    }

    val indexTag: Int
        get() = 0

}
