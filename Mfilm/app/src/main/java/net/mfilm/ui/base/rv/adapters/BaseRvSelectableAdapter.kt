package net.mfilm.ui.base.rv.adapters

import android.content.Context
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.IRvSelectable

/**
 * Created by MRVU on 6/28/2017.
 */

//NOTE: do not use this in load more situation
abstract class BaseRvSelectableAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?,
                                                 mCallbackOnClick: ICallbackOnClick,
                                                 mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick, mCallbackOnLongClick), IRvSelectable<V> {
    protected var mSelectableItems = mutableListOf<SelectableItem>()
    var countSelected = 0
    private var mItemsSelectable: Boolean? = null
    override var itemsSelectable: Boolean?
        get() = mItemsSelectable
        set(value) {
            mItemsSelectable = value
            countSelected = if (value == true) mSelectableItems.size else 0
            mSelectableItems.forEach {
                it.toggleSelected(value)
            }

        }

    override fun selectedItems(): List<V> {
        return mData.filterIndexed { index, v -> }
    }

    override fun obtainCountSelected(selected: Boolean) {
        countSelected += if (selected) 1 else -1
    }

    override fun onSelected(position: Int, allSelected: Boolean?): Boolean {
        allSelected?.apply {
            itemsSelectable = this
            notifyDataSetChanged()
        } ?: let {
            //the first selected
            if (itemsSelectable == null) {
                itemsSelectable = false
                if (position != -1)
                    mSelectableItems[position].toggleSelected(true, { obtainCountSelected(true) })
                notifyDataSetChanged()
            } else {
                val selected = mSelectableItems[position].selected
                selected?.apply {
                    mSelectableItems[position].toggleSelected(!this, { obtainCountSelected(!this) })
                    notifyItemChanged(position)
                }
            }
        }
        return countSelected == mSelectableItems.size
    }

    override fun clear(): Boolean {
        val x = super.clear()
        if (x) {
            mSelectableItems.clear()
            countSelected = 0
        }
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
        val l = List(size) { _ -> SelectableItem() }
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