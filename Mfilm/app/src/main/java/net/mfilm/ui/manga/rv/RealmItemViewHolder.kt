package net.mfilm.ui.manga.rv

//import net.mfilm.ui.manga.SelectableItem
import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import kotlinx.android.synthetic.main.item_search_history_content.view.*
import kotlinx.android.synthetic.main.manga_thumb.view.*
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick

/**
 * Created by tusi on 5/16/17.
 */
class RealmItemViewHolder(mContext: Context, type: Int, itemView: View,
                          mCallbackOnclick: ICallbackOnClick?, mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseSelectableItemViewHolder(mContext, type, itemView, mCallbackOnclick, mCallbackOnLongClick) {
    //not use
    override fun bindView(obj: Any?, position: Int) {

    }

    override fun bindViewSelectable(obj: Any?, position: Int, selectableItem: SelectableItem?) {
//        Timber.e("------bindView--------------$obj------------$selectableItem----------------------------")
        when (obj) {
            is MangaFavoriteRealm -> {
                itemView.run {
                    obj.coverUrl?.run { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
                    selectableItem?.run {
                        iconSelected(icon_selected, selected)
                    }
                    initOnClicked(this)
                }
            }
            is MangaHistoryRealm -> {
                itemView.run {
                    obj.coverUrl?.run { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
                    selectableItem?.run {
                        iconSelected(icon_selected, selected)
                    }
                    initOnClicked(this)
                }
            }
            is SearchQueryRealm -> {
                itemView.run {
                    tv_query.text = obj.query
                    selectableItem?.run {
                        iconSelected(icon_query_selected, selected)
                    }
                    initOnClicked(this)
                }
            }
        }
    }
}