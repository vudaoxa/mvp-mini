package net.mfilm.ui.manga.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.realm.RealmObject
import net.mfilm.R
import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.rv.adapters.BaseRvSelectableAdapter
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_FILTER
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_SEARCH_HISTORY
import net.mfilm.ui.manga.SelectableItem
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import timber.log.Timber

/**
 * Created by tusi on 6/21/17.
 */
class BaseRvRealmAdapter<V : RealmObject>(mContext: Context, mData: MutableList<V>?,
                                          mCallbackOnClick: ICallbackOnClick,
                                          mCallbackOnLongClick: ICallbackOnLongClick? = null,
                                          private val filter: Boolean = false)
    : BaseRvSelectableAdapter<V>(mContext, mData, mCallbackOnClick, mCallbackOnLongClick) {
    //do not use this method in abstract class
    init {
        mData?.apply {
            mSelectableItems = MutableList(size) { _ -> SelectableItem() }
            Timber.e("------BaseRvSelectableAdapter--------- $size----------${mSelectableItems.size}----------------------------")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var layoutId = R.layout.item_manga
        when (viewType) {
            TYPE_ITEM_SEARCH_HISTORY -> {
                layoutId = R.layout.item_search_history
            }
        }
        val view = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        return MangaRealmItemViewHolder(mContext, viewType, view, mCallbackOnClick, mCallbackOnLongClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val x = mSelectableItems[position]
        Timber.e("--onBindViewHolder-------------------$x-----------------------")
        if (holder is MangaRealmItemViewHolder) {
            mData?.get(position)?.apply {
                holder.bindViewSelectable(this, position, x)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (filter) return TYPE_ITEM_FILTER
        when (mData!![position]) {
            is SearchQueryRealm -> {
                return TYPE_ITEM_SEARCH_HISTORY
            }
            else -> return TYPE_ITEM
        }
    }
}