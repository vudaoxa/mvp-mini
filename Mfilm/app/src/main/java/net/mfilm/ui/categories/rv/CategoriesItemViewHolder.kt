package net.mfilm.ui.categories.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_category.view.*
import net.mfilm.data.network_retrofit.Category
import net.mfilm.ui.base.rv.holders.BaseViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 6/12/17.
 */
class CategoriesItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
        if (obj is Category) {
            obj.apply {
                itemView.apply {
                    tv_name.text = name
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }
}