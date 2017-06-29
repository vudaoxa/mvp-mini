package net.mfilm.ui.manga.rv

import android.content.Context
import android.view.View
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.ui.manga.SelectableItem
//import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick

/**
 * Created by tusi on 6/28/17.
 */
abstract class BaseSelectableItemViewHolder(mContext: Context, type: Int, itemView: View,
                                            mCallbackOnclick: ICallbackOnClick?, mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick, mCallbackOnLongClick) {
    abstract fun bindViewSelectable(obj: Any?, position: Int, selectableItem: SelectableItem)
//    abstract fun bindView(obj: Any?, position: Int, selectableItem: Boolean?)
}