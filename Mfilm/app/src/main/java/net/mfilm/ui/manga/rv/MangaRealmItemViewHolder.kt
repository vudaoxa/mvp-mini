package net.mfilm.ui.manga.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import kotlinx.android.synthetic.main.item_search_history_content.view.*
import kotlinx.android.synthetic.main.layout_manga_thumb.view.*
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.rv.holders.BaseViewHolder
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/16/17.
 */
class MangaRealmItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
        when (obj) {
            is MangaFavoriteRealm -> {
                itemView.apply {
                    obj.coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
            is MangaHistoryRealm -> {
                itemView.apply {
                    obj.coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
            is SearchQueryRealm -> {
                itemView.apply {
                    tv_query.text = obj.query
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }
}