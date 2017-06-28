package net.mfilm.ui.custom

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout

import net.mfilm.R


/**
 * Created by Dieu on 29/08/2016.
 */
class SimpleViewAnimator : LinearLayout {
    private var inAnimation: Animation? = null
    private var outAnimation: Animation? = null

    constructor(context: Context) : super(context) {
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view))
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view))
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view))
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_view))
    }


    fun setInAnimation(inAnimation: Animation) {
        this.inAnimation = inAnimation
    }

    fun setOutAnimation(outAnimation: Animation) {
        this.outAnimation = outAnimation
    }

    override fun setVisibility(visibility: Int) {
        if (getVisibility() != visibility) {
            if (visibility == View.VISIBLE) {
                if (inAnimation != null) startAnimation(inAnimation)
            } else if (visibility == View.INVISIBLE || visibility == View.GONE) {
                if (outAnimation != null) startAnimation(outAnimation)
            }
        }
        super.setVisibility(visibility)
    }
}
