package net.mfilm.utils

import android.app.ProgressDialog
import android.content.Context
import android.support.annotation.StringRes

import com.afollestad.materialdialogs.MaterialDialog

import net.mfilm.R

/**
 * Created by Dieu on 21/04/2017.
 */

object DialogUtil {
    fun showMessageConfirm(context: Context, @StringRes resTitle: Int,
                           @StringRes resContent: Int,
                           positiveCallback: MaterialDialog.SingleButtonCallback) {
        MaterialDialog.Builder(context)
                .title(resTitle)
                .content(resContent)
                .positiveColorRes(R.color.colorPrimary)
                .negativeColorRes(R.color.grey_60)
                .positiveText(R.string.OK)
                .negativeText(R.string.cancel)
                .onPositive(positiveCallback)
                .show()

    }

    fun getDialogShow(context: Context, @StringRes resTitle: Int, @StringRes resContent: Int): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle(resTitle)
        progressDialog.setMessage(context.getString(resContent))
        progressDialog.isIndeterminate = true
        return progressDialog
    }
}