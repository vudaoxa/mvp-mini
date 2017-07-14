package net.mfilm.ui.chapter_images.preview

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_image_preview.view.*
import net.mfilm.data.network_retrofit.ChapterImage
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackRvFailure

/**
 * Created by tusi on 7/14/17.
 */
class ImagesPreviewItemViewHolder(mContext: Context, type: Int, itemView: View,
                                  mCallbackOnclick: ICallbackOnClick?,
                                  val mCallbackRvFailure: ICallbackRvFailure? = null)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
        if (obj is ChapterImage) {
            itemView.run {
                image_preview.setImageURI(obj.url)
                tv_position.text = "${position + 1}"
                setOnClickListener { mCallbackOnClick?.onClick(position, type) }
            }
        }
    }
}