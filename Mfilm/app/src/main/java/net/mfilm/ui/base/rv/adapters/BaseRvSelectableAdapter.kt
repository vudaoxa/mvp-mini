package net.mfilm.ui.base.rv.adapters

import android.content.Context
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.IRvSelectable
import net.mfilm.utils.ItemSelections

/**
 * Created by MRVU on 6/28/2017.
 */

//NOTE: do not use this in load more situation
abstract class BaseRvSelectableAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?,
                                                 mCallbackOnClick: ICallbackOnClick,
                                                 mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick, mCallbackOnLongClick), IRvSelectable<V> {
    init {
        mData?.apply {
            mSelectableItems = MutableList(size) { _ -> SelectableItem(ItemSelections.INACTIVE) }
        }
    }

    protected var mSelectableItems = mutableListOf<SelectableItem>()
    private var mItemsSelectable = false
    override var itemsSelectable: Boolean
        get() = mItemsSelectable
        set(value) {
            mItemsSelectable = value
            val selected = if (mItemsSelectable) ItemSelections.UNSELECTED else ItemSelections.INACTIVE
            mSelectableItems.forEach { it.selected = selected }
        }

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

    //use when adapter addAll(items)
    override fun addSelectableItems(size: Int) {
        val l = List(size) { _ -> SelectableItem(ItemSelections.INACTIVE) }
        mSelectableItems.addAll(l)
    }

    //use when adapter remove items at indices
    override fun removeSelectableItems(indices: List<Int>) {
        var removed = 0
        indices.forEach {
            mSelectableItems.removeAt(it - removed)
            removed++
        }
    }

}