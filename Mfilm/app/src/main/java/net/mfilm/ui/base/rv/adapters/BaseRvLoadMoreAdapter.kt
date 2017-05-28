package net.mfilm.ui.base.rv.adapters

import android.content.Context
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_LOADING
import net.mfilm.utils.DebugLog
import net.mfilm.utils.IAdapterLoadMore
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.handler

/**
 * Created by tusi on 5/28/17.
 */
abstract class BaseRvLoadMoreAdapter<V : Any>(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick), IAdapterLoadMore {
    override fun getItemViewType(position: Int): Int {
        mData?.apply {
            val item = this[position]
            if (isMainItem(item)) return TYPE_ITEM
            return TYPE_ITEM_LOADING
        }
        return -10
    }

    override fun getItemCount() = mData?.size ?: 0
    override fun onAdapterLoadMore(f: () -> Unit) {
        DebugLog.e("-------------onAdapterLoadMore-----------------")
        if (isMoreLoading) return
        mData?.apply {
            isMoreLoading = true
            add(loadingItem)
            handler({
                notifyItemInserted(itemCount - 1)
                f()
            }, 0)
        }
    }

    override fun onAdapterLoadMoreFinished(f: () -> Unit) {
        DebugLog.e("------------------onAdapterLoadMoreFinished---------------------")
        handler({
            mData?.apply {
                val l = itemCount
                if (l > 0) {
                    removeAt(l - 1)
                    notifyItemRemoved(l - 1)
                }
            }
            isMoreLoading = false
            f()
        })
    }

    override fun reset() {
        DebugLog.e("-----------------reset-----------------")
        mData?.clear()
//        notifyDataSetChanged()
    }

    abstract fun isMainItem(item: V): Boolean
    abstract val loadingItem: V
}