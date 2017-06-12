package net.mfilm.ui.categories.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.mfilm.R
import net.mfilm.ui.base.rv.adapters.BaseRvAdapter
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 6/12/17.
 */
class CategoriesRvAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false)
        return CategoriesItemViewHolder(mContext, viewType, view, mCallbackOnClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is CategoriesItemViewHolder) {
            mData?.get(position)?.apply {
                holder.bindView(this, position)
            }
        }
    }
}