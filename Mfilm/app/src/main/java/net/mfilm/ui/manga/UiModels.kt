package net.mfilm.ui.manga

import android.view.View
import android.widget.AdapterView
import timber.log.Timber

/**
 * Created by tusi on 5/15/17.
 */
class Filter(val resId: Int, val content: String)

class NavItem(val id: Int, val indexTag: Any?)

class AdapterTracker(val f: (() -> Unit)? = null) : AdapterView.OnItemSelectedListener {
    var mPosition = 0
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mPosition = position
        Timber.e("-------------onItemSelected--- $mPosition ------------------------")
        f?.invoke()
    }
}