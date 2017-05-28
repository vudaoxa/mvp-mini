package net.mfilm.ui.base.rv.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseRvAdapter<V : Any>(val mContext: Context, var mData: MutableList<V>?)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isMoreLoading = false
    var mCallbackOnClick: ICallbackOnClick? = null

    constructor(mContext: Context, mData: MutableList<V>?, mCallbackOnClick: ICallbackOnClick) : this(mContext, mData) {
        this.mCallbackOnClick = mCallbackOnClick
    }
}