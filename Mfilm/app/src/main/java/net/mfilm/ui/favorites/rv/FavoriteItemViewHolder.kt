package net.mfilm.ui.mangas.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import kotlinx.android.synthetic.main.layout_manga_thumb.view.*
import net.mfilm.data.db.models.MangaRealm
import net.mfilm.ui.base.rv.holders.BaseViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/16/17.
 */
class FavoriteItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
        if (obj is MangaRealm) {
            obj.apply {
                itemView.apply {
                    coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = name
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }
}