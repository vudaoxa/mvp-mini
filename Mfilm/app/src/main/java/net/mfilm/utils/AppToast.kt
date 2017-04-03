package net.mfilm.utils

import android.content.Context
import android.support.annotation.StringRes
import android.widget.Toast
import es.dmoral.toasty.Toasty
import net.mfilm.di.AppContext
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

/**
 * Created by Dieu on 09/03/2017.
 */

class AppToast @Inject
constructor(@AppContext val mContext: Context) {

    private fun showMessageInfo(@NotNull message: String) {
        Toasty.info(mContext, message, Toast.LENGTH_SHORT, true).show()
    }

    private fun showMessageInfo(@StringRes resIdString: Int) {
        Toasty.info(mContext, mContext.getString(resIdString), Toast.LENGTH_SHORT, true).show()
    }


    private fun showMessageSuccess(@NotNull message: String) {
        Toasty.success(mContext, message, Toast.LENGTH_SHORT, true).show()
    }

    private fun showMessageSuccess(@StringRes resIdString: Int) {
        Toasty.success(mContext, mContext.getString(resIdString), Toast.LENGTH_SHORT, true).show()
    }


    private fun showMessageError(@NotNull message: String) {
        Toasty.error(mContext, message, Toast.LENGTH_SHORT, true).show()
    }

    private fun showMessageError(@StringRes resIdString: Int) {
        Toasty.error(mContext, mContext.getString(resIdString), Toast.LENGTH_SHORT, true).show()
    }

    private fun showMessageNomarl(@NotNull message: String) {
        Toasty.normal(mContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMessageNomarl(@StringRes resIdString: Int) {
        Toasty.normal(mContext, mContext.getString(resIdString), Toast.LENGTH_SHORT).show()
    }


    fun showMessageByType(typeToast: Int, message: String) {
        when (typeToast) {
            TYPE_TOAST_NOMART -> showMessageNomarl(message)
            TYPE_TOAST_INFOR -> showMessageInfo(message)
            TYPE_TOAST_SUCCESS -> showMessageSuccess(message)
            TYPE_TOAST_ERROR -> showMessageSuccess(message)
        }
    }
}
