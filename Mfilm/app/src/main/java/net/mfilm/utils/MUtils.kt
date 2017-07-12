package net.mfilm.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import com.joanzapata.iconify.fonts.IoniconsIcons
import com.joanzapata.iconify.fonts.IoniconsModule
import net.mfilm.MApplication
import net.mfilm.R
import net.mfilm.data.prefs.MangaSource
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.manga.Filter
import net.mfilm.ui.manga.NavItem
import timber.log.Timber
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by tusi on 4/2/17.
 */
fun sendShareIntent(context: Context, text: String?) {
    text ?: return
    val txt = text.trim()
    if (txt.isEmpty()) return
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_TEXT, txt)
    sendIntent.type = "text/plain"
    context.startActivity(sendIntent)
}
fun deleteCache(mContext: Context) {
    tryIt { deleteDir(mContext.cacheDir) }
}

private fun deleteDir(dir: File?): Boolean {
    return dir?.run {
        if (dir.isDirectory) {
            val children = dir.list()
            children.forEach {
                val success = deleteDir(File(dir, it))
                if (!success)
                    return false
            }
            dir.delete()
        } else {
            dir.delete()
        }
    } ?: false
}

fun tryIt(f: (() -> Unit)? = null, fCatch: (() -> Unit)? = null) {
    try {
        f?.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        fCatch?.invoke()
    }
}
var mangaSources: MangaSources? = null

fun tryOrExit(f: (() -> Unit)? = null) {
    try {
        f?.invoke()
    } catch (e: UninitializedPropertyAccessException) {
        e.printStackTrace()
        System.exit(0)
    }
}
fun initMangaSources(mContext: Context) {
    val titles = mContext.resources.getStringArray(R.array.manga_sources_title)
    val codes = mContext.resources.getStringArray(R.array.manga_sources_code)
    var i = 0
    val sources = titles.zip(codes, { t, c -> MangaSource(i++, t, c) })
    mangaSources = MangaSources(R.string.manga_source, sources)
}

val numberFormat = NumberFormat.getNumberInstance(Locale.US)
fun showMessage(@AppConstants.TypeToast typeToast: Int, msg: Any?) {
    MApplication.instance.showMessage(typeToast, msg)
}

fun rand(x: Int): Long = Math.round(Math.random() * x)
fun Fragment.isVisiOk() = isVisible && isAdded && isInLayout

fun setText(context: Context, tv: TextView, titleResId: Int, text: String?) {
    text?.run {
        if (!TextUtils.isEmpty(this)) {
            tv.show(true)
            when (titleResId) {
                -1 -> {
                    tv.text = getTitledText(this)
                }
                else -> {
                    tv.text = getTitledText(context, titleResId, this)
                }
            }
        } else tv.show(false)
    } ?: context.let { tv.show(false) }
}

fun handler(f: () -> Unit, x: Long = 150) {
    Handler().postDelayed({ f() }, x)
}

fun getTitledText(context: Context, @StringRes headerResId: Int, content: String): SpannableString {
    val header = context.getString(headerResId)
    val r = header + " " + content
    val res = SpannableString(r)
    res.run {
        val start = header.length + 1
        val end = start + content.length
        setSpan(StyleSpan(Typeface.BOLD), start,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return res
}

fun getTitledText(content: String): SpannableString {
    val res = SpannableString(content)
    res.run {
        val end = content.length
        setSpan(StyleSpan(Typeface.BOLD), 0,
                end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return res
}

var navs = listOf<NavItem>()
val navIds = listOf<Int>(R.id.nav_home, R.id.nav_categories, R.id.nav_fav, R.id.nav_history)
val indexTags = listOf<Any?>(IndexTags.FRAGMENT_HOME, IndexTags.FRAGMENT_CATEGORIES, IndexTags.FRAGMENT_FAV, IndexTags.FRAGMENT_HISTORY)
fun initNavs() {
    navs = navIds.zip(indexTags, { n, i -> NavItem(n, i) })
}

var filters = listOf<Filter>()
var filtersRealm = listOf<Filter>()
fun initFilters() {
    val filterAz = Filter(R.string.az, TYPE_FILTER_AZ)
    val filterViews = Filter(R.string.hottest, TYPE_FILTER_VIEWS)
    val filterTime = Filter(R.string.newest, TYPE_FILTER_TIME)
    filters = listOf(filterViews, filterTime, filterAz)
    filtersRealm = listOf(filterTime, filterAz)
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
    anim?.run {
        duration = ANIM_DURATION_SHORT
    }

    animIn = AnimationUtils.loadAnimation(context,
            android.R.anim.fade_in)
    animOut = AnimationUtils.loadAnimation(context,
            android.R.anim.fade_out)
    animIn?.run {
        duration = ANIM_DURATION
    }
    animOut?.run {
        duration = ANIM_DURATION
    }
}

var icon_search: IconDrawable? = null
var icon_share: IconDrawable? = null
var icon_checked_grey: IconDrawable? = null
var icon_star: IconDrawable? = null
//var icon_star_grey: IconDrawable? = null
var icon_star_blue: IconDrawable? = null
//var icon_send: IconDrawable? = null
//var icon_del: IconDrawable? = null
var icon_left_small: IconDrawable? = null
var icon_right_small: IconDrawable? = null
var icon_close: IconDrawable? = null
var icon_up: IconDrawable? = null
var icon_down: IconDrawable? = null
var icon_left: IconDrawable? = null
var icon_right: IconDrawable? = null

fun initIcons(context: Context) {
    icon_search = IconDrawable(context,
            IoniconsIcons.ion_ios_search_strong).colorRes(R.color.white).actionBarSize()
    icon_share = IconDrawable(context,
            IoniconsIcons.ion_share).colorRes(R.color.white).actionBarSize()
    icon_checked_grey = IconDrawable(context,
            IoniconsIcons.ion_checkmark_circled).colorRes(R.color.grey_60).actionBarSize()
    icon_star = IconDrawable(context,
            IoniconsIcons.ion_ios_star).colorRes(R.color.white).actionBarSize()
    icon_star_blue = IconDrawable(context,
            IoniconsIcons.ion_ios_star).colorRes(R.color.blue).actionBarSize()

    icon_left_small = IconDrawable(context,
            IoniconsIcons.ion_chevron_left).colorRes(R.color.white).actionBarSize()
    icon_right_small = IconDrawable(context,
            IoniconsIcons.ion_chevron_right).colorRes(R.color.white).actionBarSize()

//    icon_star_grey = IconDrawable(context,
//            IoniconsIcons.ion_ios_star).colorRes(R.color.grey_60).actionBarSize()
//    icon_send = IconDrawable(context,
//            IoniconsIcons.ion_android_send).colorRes(R.color.white).actionBarSize()
//    icon_del = IconDrawable(context,
//            IoniconsIcons.ion_ios_trash).colorRes(R.color.red).actionBarSize()
    icon_close = IconDrawable(context,
            IoniconsIcons.ion_ios_close_empty).colorRes(R.color.white).actionBarSize()

    icon_up = IconDrawable(context,
            IoniconsIcons.ion_chevron_up).colorRes(R.color.white).sizeDp(BIG_BTN_SIZE)
    icon_down = IconDrawable(context,
            IoniconsIcons.ion_chevron_down).colorRes(R.color.white).sizeDp(BIG_BTN_SIZE)
    icon_left = IconDrawable(context,
            IoniconsIcons.ion_chevron_left).colorRes(R.color.white).sizeDp(BIG_BTN_SIZE)
    icon_right = IconDrawable(context,
            IoniconsIcons.ion_chevron_right).colorRes(R.color.white).sizeDp(BIG_BTN_SIZE)
}

const val BIG_BTN_SIZE = 60
fun View?.enable(enable: Boolean) {
    this?.run {
        isEnabled = enable
    }
}

fun View?.toggleShow() {
    show(!isVisible())
}
fun View?.show(show: Boolean, duration: Long? = null, f: (() -> Unit)? = null): CountDownTimer? {
    this?.run {
        if (show) {
            if (isVisible()) return null
            startAnimation(anim)
            postOnAnimationDelayed({ visibility = visible }, 250)
            duration?.run {
                return schedule(this, {
                    f?.invoke()
                })
            }
        } else {
            visibility = gone
        }
    }
    return null
}

fun schedule(duration: Long, f: (() -> Unit)? = null): CountDownTimer {
    val timer = object : CountDownTimer(duration, 1000) {
        override fun onTick(p0: Long) {
            Timber.d("------------------- ------ onTick---- $p0")
        }

        override fun onFinish() {
            f?.invoke()
        }
    }
    timer.start()
    return timer
}

fun Int.isEven() = this % 2 == 0
fun View?.isVisible(): Boolean {
    return this?.run {
        visibility == visible
    } ?: false
}

fun showLoadingDialog(context: Context): ProgressDialog {
    val progressDialog = ProgressDialog(context)
    progressDialog.run {
        show()
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.progress_dialog)
        isIndeterminate = true
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }
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