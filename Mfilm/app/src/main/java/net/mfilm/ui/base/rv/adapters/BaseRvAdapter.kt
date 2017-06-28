package net.mfilm.ui.base.rv.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackOnLongClick
import net.mfilm.utils.IRV

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseRvAdapter<V : Any?>(val mContext: Context, var mData: MutableList<V>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IRV {

    var mCallbackOnClick: ICallbackOnClick? = null
    var mCallbackOnLongClick: ICallbackOnLongClick? = null
    constructor(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick? = null) : this(mContext, mData) {
        this.mCallbackOnClick = mCallbackOnClick
    }

    constructor(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick? = null,
                mCallbackOnLongClick: ICallbackOnLongClick? = null) : this(mContext, mData, mCallbackOnClick) {
        this.mCallbackOnLongClick = mCallbackOnLongClick
    }
    override fun getItemCount() = mData?.size ?: 0
    override fun clear(): Boolean {
        mData?.clear() ?: return false
        return true
    }
}