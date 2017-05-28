package net.mfilm.ui.chapters.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.ui.base.rv.holders.BaseViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/27/17.
 */
class ChaptersItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is Chapter) {
            obj.apply {
                itemView.apply {
                    tv_name.text = name
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }
}