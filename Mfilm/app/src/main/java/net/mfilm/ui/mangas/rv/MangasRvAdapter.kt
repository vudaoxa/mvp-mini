package net.mfilm.ui.mangas.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.lc.holders.LoadingViewHolder
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.rv.adapters.BaseRvLoadMoreAdapter
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/16/17.
 */
class MangasRvAdapter<V : Manga>(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvLoadMoreAdapter<V>(mContext, mData, mCallbackOnClick) {
    override fun isMainItem(item: V): Boolean {
        return item.id != null
    }

    override val loadingItem: V
        get() = Manga() as V

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_ITEM -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_manga, parent, false)
                return MangaItemViewHolder(mContext, viewType, view, mCallbackOnClick)
            }
            else -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.layout_progress_view_small, parent, false)
                return LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is MangaItemViewHolder -> {
                mData?.getOrNull(position)?.apply {
                    holder.bindView(this, position)
                }
            }
            is LoadingViewHolder -> {
                holder.isIndeterminate = true
            }
        }
    }
}