package net.mfilm.ui.custom

import android.view.View
import net.mfilm.ui.manga.Filter
import net.mfilm.utils.ISwitch

/**
 * Created by tusi on 4/7/17.
 */
class SwitchButtons(private val switchButtonItems: List<SwitchButtonItem>, f: (Int) -> Unit) : ISwitch {
    init {
        setOnClickListener(f)
    }

    override val size: Int
        get() = switchButtonItems.filter { it.enabled }.size

    override fun onSwitch(i: Int) {
        switchButtonItems[i].btn.isSelected = true
        for (j in switchButtonItems.indices.filter { it != i }) {
            switchButtonItems[j].btn.isSelected = false
        }
    }

    override fun setOnClickListener(f: (Int) -> Unit) {
        for (i in switchButtonItems.indices) {
            switchButtonItems[i].setOnClickListener {
                //all below tasks are f() parameter we passed to setOnClickListener function

                //change viewpager current item
                f(i)
                //enable(false) others btns
                onClick(i)
                //all of it will be execute as f() at @line 36
            }
        }
    }

    override fun onClick(i: Int) {
        for (j in switchButtonItems.indices.filter { it != i }) {
            switchButtonItems[j].onClick(false)
        }
    }
}

class SwitchButtonItem(val i: Int, val btn: View, val filter: Filter, val enabled: Boolean) {
    init {
        if (i == 0) {
            btn.isSelected = enabled
        }
    }

    fun setOnClickListener(f: () -> Unit) {
        btn.setOnClickListener {
            if (enabled) {
                f.invoke()
                onClick(true)
            }
        }
    }

    fun onClick(selected: Boolean) {
        btn.isSelected = selected
//        Timber.e("$i-------------------------$selected")
    }
}

