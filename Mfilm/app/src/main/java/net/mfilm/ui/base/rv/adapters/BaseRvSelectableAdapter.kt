package net.mfilm.ui.base.rv.adapters

import android.content.Context
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.IRvSelectable
import timber.log.Timber

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
    //use for select/deselect all, done
    override var itemsSelectable: Boolean?
        get() = mItemsSelectable
        set(value) {
            mItemsSelectable = value
            countSelected = if (value == true) mSelectableItems.size else 0
            mSelectableItems.forEach {
                it.toggleSelected(value)
            }
        }

    override fun onOriginal() {
        itemsSelectable = null
        notifyDataSetChanged()
    }
    private var selectedIndices: List<Int>? = null
    override fun selectedItems(): List<IndexedValue<V>>? {
        selectedIndices = mSelectableItems.indices.filter { mSelectableItems[it].selected == true }
        return selectedIndices?.run {
            mData?.withIndex()?.filter { it.index in this }
        }
    }

    override fun obtainCountSelected(selected: Boolean) {
        countSelected += if (selected) 1 else -1
    }

    override fun onSelected(position: Int, allSelected: Boolean?): Boolean {
        Timber.e("-----onSelected----------$position---------------$allSelected-----------------------------")
        allSelected?.run {
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
                selected?.run {
                    mSelectableItems[position].toggleSelected(!this, { obtainCountSelected(!this) })
                    notifyItemChanged(position)
                }
            }
        }
        return countSelected == mSelectableItems.size
    }

    override fun addAll(items: List<V>?): Boolean {
        val x = super.addAll(items)
        if (x) {
            items?.run {
                addSelectableItems(size)
            }
        }
        return x
    }

    override fun recoverAll(elements: List<V>?): Boolean {
        val x = super.addAll(elements)
        if (x) {
            selectedItems?.run {
                val y = mSelectableItems.addAll(this)
                if (y) countSelected += this.size
                return y
            }
        }
        return x
    }

    override fun clear(): Boolean {
        val x = super.clear()
        if (x) {
            mSelectableItems.clear()
            countSelected = 0
        }
        return x
    }

    private var selectedItems: List<SelectableItem>? = null
    override fun removeAll(elements: List<V>?): Boolean {
        val x = super.removeAll(elements)
        if (x) {
            selectedIndices?.run {
                selectedItems = mSelectableItems.filterIndexed { index, _ -> index in this }
                selectedItems?.run {
                    val y = mSelectableItems.removeAll(this)
                    if (y) countSelected -= this.size
                    return y
                }
            }
        }
        return x
    }

    override fun addSelectableItem(item: SelectableItem) {
        mSelectableItems.add(item)
    }

    //use when adapter addAll(items)
    override fun addSelectableItems(size: Int) {
        val l = List(size) { _ -> SelectableItem() }
        mSelectableItems.addAll(l)
    }
}