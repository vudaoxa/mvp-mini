package net.mfilm.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.provider.Settings
import net.mfilm.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tusi on 4/2/17.
 */
fun showLoadingDialog(context: Context): ProgressDialog {
    val progressDialog = ProgressDialog(context)
    progressDialog.show()
    if (progressDialog.window != null) {
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    progressDialog.setContentView(R.layout.progress_dialog)
    progressDialog.isIndeterminate = true
    progressDialog.setCancelable(true)
    progressDialog.setCanceledOnTouchOutside(false)
    return progressDialog
}

@SuppressLint("all")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}


fun getTimeStamp(): String {
    return SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())
}

fun isNetConnected(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}