package net.mfilm.ui.base.rv.adapters

import android.content.Context
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.IRvSelectable
import net.mfilm.utils.ItemSelections

/**
 * Created by MRVU on 6/28/2017.
 */

//NOTE: do not use this in load more situation
abstract class BaseRvSelectableAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?)
    : BaseRvAdapter<V>(mContext, mData), IRvSelectable<V> {
    init {
        mData?.apply {
            addSelectableItems(size)
        }
    }

    var mSelectableItems = mutableListOf<SelectableItem>()
    var itemSelectable = false
    override fun clear(): Boolean {
        val x = super.clear()
        if (x)
            mSelectableItems.clear()
        return x
    }

    override fun addSelectableItem(item: SelectableItem) {
        mSelectableItems.add(item)
    }

    //not use
    override fun add(item: V) {

    }

    override fun addAll(items: List<V>) {
        mData?.apply {
            addAll(items)
            addSelectableItems(items.size)
        }
    }

    override fun addSelectableItems(size: Int) {
        val l = List(size) { _ -> SelectableItem(ItemSelections.INACTIVE) }
        mSelectableItems.addAll(l)
    }
}