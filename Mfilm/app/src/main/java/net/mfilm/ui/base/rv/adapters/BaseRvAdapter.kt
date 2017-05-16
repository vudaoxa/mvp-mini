package net.mfilm.ui.base.rv.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 5/16/2017.
 */
abstract class BaseRvAdapter(val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isLoading = false
    var isFirstData = true
    var mCallbackOnClick: ICallbackOnClick? = null

    constructor(mContext: Context, mCallbackOnClick: ICallbackOnClick) : this(mContext) {
        this.mCallbackOnClick = mCallbackOnClick
    }

//    fun xx(f: () -> Unit) {
//        if (isFirstData) {
//            isFirstData = false
//            f()
//        } else {
//
//        }
//    }
}