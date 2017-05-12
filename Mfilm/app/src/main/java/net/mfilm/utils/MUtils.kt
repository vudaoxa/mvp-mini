package net.mfilm.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.provider.Settings
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import net.mfilm.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tusi on 4/2/17.
 */
var anim: Animation? = null
var animIn: Animation? = null
var animOut: Animation? = null
//var rotate: RotateAnimation?=null
//var rotateReversed: RotateAnimation?=null
fun initAnimations(context: Context) {
    if (anim != null) {
        return
    }
    anim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    anim?.apply {
        duration = ANIM_DURATION_SHORT
    }

    animIn = AnimationUtils.loadAnimation(context,
            android.R.anim.fade_in)
    animOut = AnimationUtils.loadAnimation(context,
            android.R.anim.fade_out)
    animIn?.apply {
        duration = ANIM_DURATION
    }
    animOut?.apply {
        duration = ANIM_DURATION
    }
}

fun View.show(show: Boolean) {
    if (show) {
        visibility = View.VISIBLE
        startAnimation(anim)
    } else {
        visibility = View.GONE
    }
}
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