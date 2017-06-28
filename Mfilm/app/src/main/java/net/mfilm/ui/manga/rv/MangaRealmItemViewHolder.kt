package net.mfilm.ui.manga.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import kotlinx.android.synthetic.main.item_search_history_content.view.*
import kotlinx.android.synthetic.main.layout_manga_thumb.view.*
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick

/**
 * Created by tusi on 5/16/17.
 */
class MangaRealmItemViewHolder(mContext: Context, type: Int, itemView: View,
                               mCallbackOnclick: ICallbackOnClick?, mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseSelectableItemViewHolder(mContext, type, itemView, mCallbackOnclick, mCallbackOnLongClick) {
    override fun bindView(obj: Any?, position: Int) {

    }

    override fun bindView(obj: Any?, position: Int, selectableItem: SelectableItem) {
        when (obj) {
            is MangaFavoriteRealm -> {
                itemView.apply {
                    obj.coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
//                    val iconSelectedShow = selectableItem.selected != ItemSelections.INACTIVE
//                    icon_selected.show(iconSelectedShow)
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                    setOnLongClickListener {
                        mCallbackOnLongClick?.onLongClick(position, type)
                        true
                    }
                }
            }
            is MangaHistoryRealm -> {
                itemView.apply {
                    obj.coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                    setOnLongClickListener {
                        mCallbackOnLongClick?.onLongClick(position, type)
                        true
                    }
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