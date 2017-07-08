package com.icom.xsvietlott.ui.vlot

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.CompoundButton
import com.afollestad.materialdialogs.MaterialDialog
import net.mfilm.R
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.anim

/**
 * Created by tusi on 4/13/17.
 */
class DialogInputTarget(val mContext: Context, val adapter: RecyclerView.Adapter<BaseItemViewHolder>,
                        val layoutManager: RecyclerView.LayoutManager, var fullScreen: Boolean = false) {
    var onCheckedChangeListener: CompoundButton.OnCheckedChangeListener? = null
    lateinit var mCallbackOnSubmit: ICallbackOnClick
    var dialogBuilder: MaterialDialog.Builder? = null
    var dialog: MaterialDialog? = null
    var content = ""

    constructor(mContext: Context, adapter: RecyclerView.Adapter<BaseItemViewHolder>,
                layoutManager: RecyclerView.LayoutManager, mCallbackOnSubmit: ICallbackOnClick)
            : this(mContext, adapter, layoutManager) {
        this.mCallbackOnSubmit = mCallbackOnSubmit
    }

    constructor(mContext: Context, adapter: RecyclerView.Adapter<BaseItemViewHolder>,
                layoutManager: RecyclerView.LayoutManager, mCallbackOnSubmit: ICallbackOnClick,
                onCheckedChangeListener: CompoundButton.OnCheckedChangeListener, fullScreen: Boolean = false)
            : this(mContext, adapter, layoutManager, mCallbackOnSubmit) {
        this.onCheckedChangeListener = onCheckedChangeListener
        this.fullScreen = fullScreen
    }

    //for MapActivity
    fun buildDialog() {
        if (dialogBuilder == null) {
            dialogBuilder =
                    MaterialDialog.Builder(mContext)
                            .adapter(adapter, layoutManager)
                            .backgroundColorRes(R.color.navy_blue_dark)
        }
    }

    fun buildDialogMultiChoice() {
//        if (dialogBuilder == null) {
//            dialogBuilder =
//                    MaterialDialog.Builder(mContext)
//                            .adapter(adapter, layoutManager)
//                            .backgroundColorRes(R.color.navy_blue_dark)
//                            .contentColorRes(R.color.red)
//                            .checkBoxPrompt(mContext.getString(R.string.random), mChecked, onCheckedChangeListener)
//                            .negativeText(R.string.cancel)
//                            .negativeColorRes(R.color.grey_60)
//                            .positiveText(R.string.OK)
//                            .onPositive { _, _ ->
//                                run {
//                                    dismiss()
//                                    mCallbackOnSubmit.onClick(0, ACTION_CLICK_MULTI_CHOICE_SUBMIT)
//                                }
//                            }
//        }
    }

    fun initFullScreenDialog() {
        if (!fullScreen) return
        dialog?.apply {
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        }
    }

    fun showDialog() {
        dialog?.show() ?: let {
            dialog = dialogBuilder?.show()
            initFullScreenDialog()
        }
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    fun content(content: String) {
        dialog?.setContent(content)
    }

    fun content(content: Int) {
        dialog?.apply {
            setContent(content)
            contentView?.apply {
                startAnimation(anim)
            }
        }
    }

    var mChecked = false
    fun setChecked(checked: Boolean) {
        mChecked = checked
        dialog?.apply {
            isPromptCheckBoxChecked = checked
        }
    }
}