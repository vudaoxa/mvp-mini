package net.mfilm.ui.base.rv.holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 4/5/17.
 */
abstract class BaseViewHolder(protected val mContext: Context, val type: Int = -1,
                              itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mCallbackOnClick: ICallbackOnClick? = null

    constructor(mContext: Context, type: Int, itemView: View, mCallbackOnClick: ICallbackOnClick?)
            : this(mContext, type, itemView) {
        this.mCallbackOnClick = mCallbackOnClick
    }

    abstract fun bindView(obj: Any, position: Int)

    var mPosition: Int = 0
}

const val ACTION_CLICK_ITEM_SELECT_NUM = 0
const val ACTION_CLICK_ITEM_INPUT_NUM = 1
const val ACTION_CLICK_ITEM_SELECT_DATE = 2
const val ACTION_CLICK_ITEM_INPUT_DATE = 3
const val ACTION_CLICK_ITEM_SINGLE_CHOICE = 4
const val ACTION_CLICK_ITEM_MULTICHOICE = 5
const val ACTION_CLICK_MULTI_CHOICE_SUBMIT = 6
const val ACTION_CLICK_LIVE_ITEM = 7

const val TYPE_ITEM_MANGA = 0
const val TYPE_ITEM_LOADING = -1