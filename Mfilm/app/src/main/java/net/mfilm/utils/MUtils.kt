package net.mfilm.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.joanzapata.iconify.fonts.IoniconsModule
import net.mfilm.R
import net.mfilm.ui.manga.Filter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tusi on 4/2/17.
 */
fun getTitleHeader(context: Context, headerResId: Int, contentResId: Int): SpannableString {
    val header = context.getString(headerResId)
    val content = context.getString(contentResId)
    val r = header + " " + content
    val res = SpannableString(r)
    res.apply {
        val start = header.length + 1
        val end = start + content.length
        setSpan(StyleSpan(Typeface.BOLD), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return res
}

fun getTitleHeader(context: Context, headerResId: Int, content: String): SpannableString {
    val header = context.getString(headerResId)
    val r = header + " " + content
    val res = SpannableString(r)
    res.apply {
        val start = header.length + 1
        val end = start + content.length
        setSpan(StyleSpan(Typeface.BOLD), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return res
}

var filters = listOf<Filter>()
fun initFilters() {
    val filterAz = Filter(R.string.az, TYPE_FILTER_AZ)
    val filterViews = Filter(R.string.hottest, TYPE_FILTER_VIEWS)
    val filterTime = Filter(R.string.newest, TYPE_FILTER_TIME)
    filters = listOf(filterAz, filterViews, filterTime)
}
fun initAwesome() {
    Iconify.with(FontAwesomeModule())
            .with(IoniconsModule())
}
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

var icon_search: IconDrawable? = null
var icon_share: IconDrawable? = null
var icon_star: IconDrawable? = null
var icon_star_blue: IconDrawable? = null
var icon_send: IconDrawable? = null
var icon_del: IconDrawable? = null
fun initIcons(context: Context) {
    icon_search = IconDrawable(context,
            IoniconsIcons.ion_ios_search_strong).colorRes(R.color.white).actionBarSize()
    icon_share = IconDrawable(context,
            IoniconsIcons.ion_share).colorRes(R.color.grey_60).actionBarSize()
    icon_star = IconDrawable(context,
            IoniconsIcons.ion_ios_star).colorRes(R.color.grey_60).actionBarSize()
    icon_star_blue = IconDrawable(context,
            IoniconsIcons.ion_ios_star).colorRes(R.color.blue).actionBarSize()
    icon_send = IconDrawable(context,
            IoniconsIcons.ion_android_send).colorRes(R.color.white).actionBarSize()
    icon_del = IconDrawable(context,
            IoniconsIcons.ion_ios_trash).colorRes(R.color.red).actionBarSize()
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