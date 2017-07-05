package net.mfilm.utils

import android.content.Context
import android.view.Gravity
import com.orhanobut.dialogplus.DialogPlus
import net.mfilm.R
import net.mfilm.ui.dialog_menus.DialogMenuAdapter
import net.mfilm.ui.manga.CallbackLongClickItem
import timber.log.Timber

/**
 * Created by MRVU on 7/5/2017.
 */
object DialogUtils {
    private var dialog: DialogPlus? = null
    fun showBottomDialog(mContext: Context, adapter: DialogMenuAdapter,
                         dataItemPosition: Int, longClickItem: CallbackLongClickItem?) {
        dialog = DialogPlus.newDialog(mContext)
                .setMargin(30, 0, 30, 0)
                .setAdapter(adapter)
                .setOnItemClickListener { dialog, item, view, position ->
                    Timber.e("----onlick------------$position--------------------------")
                    longClickItem?.onDialogItemClicked(dataItemPosition,
                            position, adapter.getItem(position)?.event)
                    dismiss()
                }
                .setOverlayBackgroundResource(R.color.colorOverlayBackground)
                .setContentBackgroundResource(R.drawable.bg_white_circle)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setInAnimation(R.anim.slide_up)
                .setExpanded(false)
                .setOnBackPressListener {
                    dismiss()
                }.create()
        dialog?.show()
    }

    fun dismiss() {
        dialog?.run {
            if (isShowing)
                dismiss()
        }
    }
}