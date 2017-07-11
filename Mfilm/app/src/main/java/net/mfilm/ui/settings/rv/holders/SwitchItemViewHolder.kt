package net.mfilm.ui.settings.rv.holders

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_switch.view.*
import net.mfilm.data.prefs.SwitchItem
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 7/7/2017.
 */
class SwitchItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
        if (obj is SwitchItem) {
            itemView.run {
                tv_name.setText(obj.titleResId)
                sw_checked.isChecked = obj.enabled
                sw_checked.setOnCheckedChangeListener { _, _ -> mCallbackOnClick?.onClick(position, type) }
            }
        }
    }
}