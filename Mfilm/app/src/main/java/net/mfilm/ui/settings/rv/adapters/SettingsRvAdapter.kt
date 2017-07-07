package net.mfilm.ui.settings.rv.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.mfilm.R
import net.mfilm.ui.base.rv.adapters.BaseRvAdapter
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_HEADER
import net.mfilm.ui.settings.rv.holders.SpinnerItemViewHolder
import net.mfilm.ui.settings.rv.holders.SwitchItemViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 7/7/2017.
 */
class SettingsRvAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            TYPE_ITEM_HEADER -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_spinner, parent, false)
                SpinnerItemViewHolder(mContext, viewType, view, mCallbackOnClick)
            }
            TYPE_ITEM -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_switch, parent, false)
                SwitchItemViewHolder(mContext, viewType, view, mCallbackOnClick)
            }
            else -> null
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        mData?.get(position)?.run {
            when (holder) {
                is SpinnerItemViewHolder -> {
                    holder.bindView(this, position)
                }
                is SwitchItemViewHolder -> {
                    holder.bindView(this, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_ITEM_HEADER else TYPE_ITEM
    }
}