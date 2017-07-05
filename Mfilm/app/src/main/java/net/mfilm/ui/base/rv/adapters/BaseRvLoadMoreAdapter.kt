package net.mfilm.ui.base.rv.adapters

import android.content.Context
import net.mfilm.ui.base.rv.holders.TYPE_ITEM
import net.mfilm.ui.base.rv.holders.TYPE_ITEM_LOADING
import net.mfilm.utils.IAdapterLoadMore
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.handler
import timber.log.Timber

/**
 * Created by tusi on 5/28/17.
 */
abstract class BaseRvLoadMoreAdapter<V : Any?>(mContext: Context, mData: MutableList<V>?,
                                               mCallbackOnClick: ICallbackOnClick,
                                               mCallbackOnLongClick: ICallbackOnLongClick? = null)
    : BaseRvAdapter<V>(mContext, mData, mCallbackOnClick, mCallbackOnLongClick), IAdapterLoadMore {
    var isMoreLoading = false
    override fun getItemViewType(position: Int): Int {
        return mData?.run {
            val item = this[position]
            if (isMainItem(item)) TYPE_ITEM
            else TYPE_ITEM_LOADING
        } ?: -10
    }

    override fun onAdapterLoadMore(f: (() -> Unit)?) {
        Timber.e("-------------onAdapterLoadMore----------$isMoreLoading-------")
        if (isMoreLoading) return
        mData?.run {
            isMoreLoading = true
            add(loadingItem)
            handler({
                notifyItemInserted(itemCount - 1)
                f?.invoke()
            }, 0)
        }
    }

    override fun onAdapterLoadMoreFinished(f: (() -> Unit)?) {
        handler({
            mData?.run {
                val l = itemCount
                if (l > 0) {
                    if (isMainItem(last())) return@run
                    remove(last())
                    notifyItemRemoved(l - 1)
                }
            }
            isMoreLoading = false
            Timber.e("------------------onAdapterLoadMoreFinished----------------$isMoreLoading-----")
            f?.invoke()
        })
    }

    override fun reset(notify: Boolean?) {
        Timber.e("-----------------reset-----------------")
        mData?.clear()
        notify?.run {
            if (this) notifyDataSetChanged()
        }
    }

    abstract fun isMainItem(item: V): Boolean
    abstract val loadingItem: V
}