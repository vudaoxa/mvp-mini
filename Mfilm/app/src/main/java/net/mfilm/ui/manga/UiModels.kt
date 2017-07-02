package net.mfilm.ui.manga

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import net.mfilm.utils.ICallbackEmptyDataView
import net.mfilm.utils.ISelectable
import net.mfilm.utils.show
import org.angmarch.views.NiceSpinner
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

class EmptyDataView(val context: Context, val spnFilter: NiceSpinner?, val layoutEmptyData: View?,
                    val tvDesEmptyData: TextView?, val emptyDesResId: Int)
    : ICallbackEmptyDataView {
    override fun hideSomething() {
        spnFilter.show(false)
    }

    override fun showEmptyDataView(show: Boolean) {
        layoutEmptyData?.show(show)
        if (show)
            tvDesEmptyData?.text = context.getText(emptyDesResId)
    }
}

//class SelectableItem(var selected: ItemSelections)
class SelectableItem(var selected: Boolean? = null) : ISelectable {
    override fun toggleSelected(selected: Boolean?, f: (() -> Unit)?) {
        this.selected = selected
        f?.invoke()
    }
}

class UndoBtn(private var undo: Boolean? = null, private var btnUndo: Button, val f: (() -> Unit)? = null) {
    init {
        btnUndo.setOnClickListener {
            undo = true
            f?.invoke()
        }
    }

    fun reset() {
        undo = null
        btnUndo.show(false)
    }

    fun onUndo(undo: Boolean) {
        this.undo = undo
    }

    fun onSelected(fTrue: (() -> Unit)? = null, fFalse: (() -> Unit)? = null, fNull: (() -> Unit)? = null) {
        Timber.e("-----undo: $undo---------------------------------")
        when (undo) {
            true -> {
                //btnUndo clicked
                btnUndo.show(false)
                undo = null
                fTrue?.invoke()
            }
            false -> {
                //after delete selected items
                btnUndo.show(true)
                fFalse?.invoke()
            }
            null -> {
                fNull?.invoke()
            }

        }
    }
}

class PassByTime(private var duration: Long = -1L, private var time: Long = -1L) {
    fun passByTime(f: (() -> Unit)? = null) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - time > duration) {
            time = currentTime
        } else return
        f?.invoke()
    }
}