package net.mfilm.ui.chapter_images.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.mfilm.R
import net.mfilm.ui.base.rv.adapters.BaseRvAdapter
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_PREVIEW
import net.mfilm.ui.chapter_images.preview.ImagesPreviewItemViewHolder
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackRvFailure

/**
 * Created by MRVU on 7/11/2017.
 */
class ChapterImagesRvAdapter<V : Any?>(mContext: Context,
                                       mData: MutableList<V>?,
                                       mCallbackOnClick: ICallbackOnClick,
                                       val mICallbackRvFailure: ICallbackRvFailure? = null, val preview: Boolean? = null)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick) {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        return when (viewType) {
            TYPE_ITEM -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_big_image, parent, false)
                ChapterImagesItemViewHolder(mContext, viewType, view, mCallbackOnClick, mICallbackRvFailure)
            }
            TYPE_ITEM_PREVIEW -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_image_preview, parent, false)
                ImagesPreviewItemViewHolder(mContext, viewType, view, mCallbackOnClick)
            }
            else -> null
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        mData?.get(position)?.run {
            when (holder) {
                is ChapterImagesItemViewHolder -> {
                    holder.bindView(this, position)

                }
                is ImagesPreviewItemViewHolder -> {
                    holder.bindView(this, position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (preview == true) TYPE_ITEM_PREVIEW else TYPE_ITEM
    }
}