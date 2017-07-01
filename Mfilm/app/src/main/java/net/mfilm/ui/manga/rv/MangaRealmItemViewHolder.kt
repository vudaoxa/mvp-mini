package net.mfilm.ui.manga.rv

//import net.mfilm.ui.manga.SelectableItem
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.joanzapata.iconify.widget.IconTextView
import kotlinx.android.synthetic.main.item_manga.view.*
import kotlinx.android.synthetic.main.item_search_history_content.view.*
import kotlinx.android.synthetic.main.layout_manga_thumb.view.*
import net.mfilm.R
import net.mfilm.data.db.models.MangaFavoriteRealm
import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.show

/**
 * Created by tusi on 5/16/17.
 */
class MangaRealmItemViewHolder(mContext: Context, type: Int, itemView: View,
                               mCallbackOnclick: ICallbackOnClick?, mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseSelectableItemViewHolder(mContext, type, itemView, mCallbackOnclick, mCallbackOnLongClick) {
    override fun bindView(obj: Any?, position: Int) {
        when (obj) {
            is MangaFavoriteRealm -> {
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

    override fun bindViewSelectable(obj: Any?, position: Int, selectableItem: SelectableItem?) {
//        Timber.e("------bindView--------------$obj------------$selectableItem----------------------------")
        when (obj) {
            is MangaFavoriteRealm -> {
                itemView.apply {
                    obj.coverUrl?.apply { img_thumb.setImageURI(this) }
                    tv_name.text = obj.name
                    selectableItem?.apply {
                        iconSelected(icon_selected, selected)
                    }
                    initOnClicked(this)
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

    fun iconSelected(iconSelected: IconTextView, selectableItem: Boolean?) {
        iconSelected.show(selectableItem != null)
        selectableItem?.apply {
            var color = R.color.grey_60
            if (this)
                color = R.color.orange
            iconSelected.setTextColor(ContextCompat.getColor(mContext, color))
        }
    }

    fun initOnClicked(itemView: View) {
        itemView.apply {
            setOnClickListener { mCallbackOnClick?.onClick(position, type) }
            setOnLongClickListener {
                mCallbackOnLongClick?.onLongClick(position, type)
                true
            }
        }
    }
}