package net.mfilm.ui.manga.rv

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.joanzapata.iconify.widget.IconTextView
import net.mfilm.R
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.ui.manga.SelectableItem
//import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.show

/**
 * Created by tusi on 6/28/17.
 */
abstract class BaseSelectableItemViewHolder(mContext: Context, type: Int, itemView: View,
                                            mCallbackOnclick: ICallbackOnClick?, mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick, mCallbackOnLongClick) {
    abstract fun bindViewSelectable(obj: Any?, position: Int, selectableItem: SelectableItem? = null)
    fun iconSelected(iconSelected: IconTextView, selectableItem: Boolean?) {
        iconSelected.show(selectableItem != null)
        selectableItem?.run {
            var color = R.color.grey_60
            if (this)
                color = R.color.orange
            iconSelected.setTextColor(ContextCompat.getColor(mContext, color))
        }
    }
}