package net.mfilm.ui.chapter_images.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.mfilm.R
import net.mfilm.ui.base.rv.adapters.BaseRvAdapter
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 7/11/2017.
 */
class ChapterImagesRvAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_big_image, parent, false)
        return ChapterImagesItemViewHolder(mContext, viewType, view, mCallbackOnClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ChapterImagesItemViewHolder) {
            mData?.get(position)?.run {
                holder.bindView(this, position)
            }
        }
    }
}