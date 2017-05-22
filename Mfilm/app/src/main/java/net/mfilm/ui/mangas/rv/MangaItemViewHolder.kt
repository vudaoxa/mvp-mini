package net.mfilm.ui.mangas.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import kotlinx.android.synthetic.main.layout_manga_thumb.view.*
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.rv.holders.BaseViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/16/17.
 */
class MangaItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is Manga) {
            obj.apply {
                itemView.apply {
                    coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = name
//                    setText(context, tv_other_name, R.string.title_other_name, otherName)
//                    setText(context, tv_author, R.string.title_author, author)
//                    setText(context, tv_categories, R.string.title_categories, categories?.map { it.name }?.joinToString())
//                    setText(context, tv_chaps_count, R.string.title_chaps_count, totalChap?.toString())
//                    setText(context, tv_updated_at, R.string.title_updated_at, TimeUtils.toFbFormatTime(mContext, updatedTime))
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }
}