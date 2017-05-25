package net.mfilm.ui.mangas.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.lc.holders.LoadingViewHolder
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.rv.adapters.BaseRvAdapter
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_LOADING
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_MANGA
import net.mfilm.utils.DebugLog
import net.mfilm.utils.IAdapterLoadMore
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.handler

/**
 * Created by tusi on 5/16/17.
 */
class MangasRvAdapter(mContext: Context, var mData: MutableList<Manga>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvAdapter(mContext, mCallbackOnClick), IAdapterLoadMore {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_ITEM_MANGA -> {
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
                mData?.get(position)?.apply {
                    holder.bindView(this, position)
                }
            }
            is LoadingViewHolder -> {
                holder.isIndeterminate = true
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        mData?.apply {
            val item = this[position]
            if (item.id != null) return TYPE_ITEM_MANGA
            return TYPE_ITEM_LOADING
        }
        return -10
    }

    override fun getItemCount() = mData?.size ?: 0

    override fun onAdapterLoadMore() {
//        DebugLog.e("-------------onAdapterLoadMore-----------------")
//        mData?.apply {
//            isMoreLoading = true
//            add(Manga())
//            Handler().post { notifyItemInserted(itemCount - 1) }
//        }
    }

    override fun onAdapterLoadMore(f: () -> Unit) {
        DebugLog.e("-------------onAdapterLoadMore-----------------")
        if (isMoreLoading) return
        mData?.apply {
            isMoreLoading = true
            add(Manga())
            handler({
                notifyItemInserted(itemCount - 1)
                f()
            }, 0)
        }
    }

    override fun onAdapterLoadMoreFinished() {
//        if (isMoreLoading) {
//            mData?.apply {
//                val l = itemCount
//                if (l > 0) {
//                    removeAt(l - 1)
//                    notifyItemRemoved(l - 1)
//                }
//            }
//            isMoreLoading = false
//        }
    }

    override fun onAdapterLoadMoreFinished(f: () -> Unit) {
        DebugLog.e("------------------onAdapterLoadMoreFinished---------------------")
        handler({
            mData?.apply {
                val l = itemCount
                if (l > 0) {
                    removeAt(l - 1)
//                    DebugLog.e("---------removeAt----------------$x")
                    notifyItemRemoved(l - 1)
//                        notifyDataSetChanged()
                }
            }
            isMoreLoading = false
            f()
        }, 0)
    }

    override fun reset() {
        DebugLog.e("-----------------reset-----------------")
        mData?.clear()
//        notifyDataSetChanged()
    }
}