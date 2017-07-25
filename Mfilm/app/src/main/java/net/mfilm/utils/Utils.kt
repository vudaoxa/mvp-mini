@file:Suppress("DEPRECATION")

package net.mfilm.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Build.VERSION_CODES
import android.support.annotation.ColorInt
import android.support.annotation.IntRange
import android.text.Html
import android.text.Spanned
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewAnimationUtils
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    val DATE_TIME = "dd MMM yyyy HH:mm:ss z"
    private var colorAccent = -1

    fun getScreenDimensions(context: Context): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        val dm = DisplayMetrics()
        display.getMetrics(dm)

        val point = Point()
        point.set(dm.widthPixels, dm.heightPixels)
        return point
    }

    fun getDisplayMetrics(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return Math.round(dp * getDisplayMetrics(context).density).toFloat()
    }

    /**
     * dd MMM yyyy HH:mm:ss z

     * @return The date formatted.
     */
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat(DATE_TIME, Locale.getDefault())
        return dateFormat.format(date)
    }

    /**
     * API 11

     * @see VERSION_CODES.HONEYCOMB
     */
    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB
    }

    /**
     * API 14

     * @see VERSION_CODES.ICE_CREAM_SANDWICH
     */
    fun hasIceCreamSandwich(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH
    }

    /**
     * API 16

     * @see VERSION_CODES.JELLY_BEAN
     */
    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN
    }

    /**
     * API 19

     * @see VERSION_CODES.KITKAT
     */
    fun hasKitkat(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT
    }

    /**
     * API 21

     * @see VERSION_CODES.LOLLIPOP
     */
    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP
    }

    /**
     * API 23

     * @see VERSION_CODES.M
     */
    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.M
    }

    /**
     * API 24

     * @see VERSION_CODES.N
     */
    fun hasNougat(): Boolean {
        return Build.VERSION.SDK_INT >= VERSION_CODES.N
    }

    fun getVersionName(context: Context): String {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return "v" + pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            return context.getString(android.R.string.unknownName)
        }

    }

    fun getVersionCode(context: Context): Int {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            return 0
        }

    }

    /**
     * Adjusts the alpha of a color.

     * @param color the color
     * *
     * @param alpha the alpha value we want to set 0-255
     * *
     * @return the adjusted color
     */
    fun adjustAlpha(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        return alpha shl 24 or (color and 0x00ffffff)
    }

//    @TargetApi(VERSION_CODES.LOLLIPOP)
//    fun getColorAccent(context: Context): Int {
//        if (colorAccent < 0) {
//            val accentAttr = if (eu.davidea.flexibleadapter.utils.Utils.hasLollipop()) android.R.attr.colorAccent else R.attr.colorAccent
//            val androidAttr = context.theme.obtainStyledAttributes(intArrayOf(accentAttr))
//            colorAccent = androidAttr.getColor(0, 0xFF009688.toInt()) //Default: material_deep_teal_500
//            androidAttr.recycle()
//        }
//        return colorAccent
//    }

    fun fromHtmlCompat(text: String): Spanned {
        if (hasNougat()) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(text)
        }
    }

    fun textAppearanceCompat(textView: TextView, resId: Int) {
        if (hasMarshmallow()) {
            textView.setTextAppearance(resId)
        } else {
            textView.setTextAppearance(textView.context, resId)
        }
    }

    /**
     * Show Soft Keyboard with new Thread

     * @param activity
     */
    fun hideSoftInput(activity: Activity) {
        if (activity.currentFocus != null) {
            Runnable {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }.run()
        }
    }

    /**
     * Hide Soft Keyboard from Dialogs with new Thread

     * @param context
     * *
     * @param view
     */
    fun hideSoftInputFrom(context: Context, view: View) {
        Runnable {
            val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }.run()
    }

    /**
     * Show Soft Keyboard with new Thread

     * @param context
     * *
     * @param view
     */
    fun showSoftInput(context: Context, view: View) {
        Runnable {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }.run()
    }

    /**
     * Create the reveal effect animation

     * @param view the View to reveal
     * *
     * @param cx   coordinate X
     * *
     * @param cy   coordinate Y
     */
    @TargetApi(VERSION_CODES.LOLLIPOP)
    fun reveal(view: View, cx: Int, cy: Int) {
        if (!hasLollipop()) {
            view.visibility = View.VISIBLE
            return
        }

        //Get the final radius for the clipping circle
        val finalRadius = Math.max(view.width, view.height)

        //Create the animator for this view (the start radius is zero)
        val animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())

        //Make the view visible and start the animation
        view.visibility = View.VISIBLE
        animator.start()
    }

    /**
     * Create the un-reveal effect animation

     * @param view the View to hide
     * *
     * @param cx   coordinate X
     * *
     * @param cy   coordinate Y
     */
    @TargetApi(VERSION_CODES.LOLLIPOP)
    fun unReveal(view: View, cx: Int, cy: Int) {
        if (!hasLollipop()) {
            view.visibility = View.GONE
            return
        }

        //Get the initial radius for the clipping circle
        val initialRadius = view.width

        //Create the animation (the final radius is zero)
        val animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)

        //Make the view invisible when the animation is done
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })

        //Start the animation
        animator.start()
    }

}