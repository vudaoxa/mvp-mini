package net.mfilm.ui.chapter_images.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.view_image_overlay.view.*

import net.mfilm.R
import net.mfilm.utils.DebugLog

/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
class ImageOverlayView : RelativeLayout {

    private var tvDescription: TextView? = null

    private var sharingText: String? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setDescription(description: String) {
        DebugLog.e("-----------------setDescription---- $description------------")
        tv_des.text = description
    }

    fun setShareText(text: String) {
        DebugLog.e("------------------setShareText---- $text------------------------")
        this.sharingText = text
    }

    private fun sendShareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharingText)
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    private fun init() {
        val view = View.inflate(context, R.layout.view_image_overlay, this)
        net.mfilm.utils.handler({
            tvDescription = view.findViewById(R.id.tv_des) as TextView
            view.findViewById(R.id.btn_share).setOnClickListener { sendShareIntent() }
            btn_share.setOnClickListener { sendShareIntent() }
        })
    }
}
