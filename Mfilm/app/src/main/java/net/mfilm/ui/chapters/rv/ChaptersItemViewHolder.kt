package net.mfilm.ui.chapters.rv

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.item_chapter.view.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Chapter
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.utils.ICallbackChaptersHistory
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.show

/**
 * Created by tusi on 5/27/17.
 */
class ChaptersItemViewHolder(mContext: Context, type: Int, itemView: View,
                             mCallbackOnclick: ICallbackOnClick?, val mCallbackChapterHistory: ICallbackChaptersHistory? = null)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any?, position: Int) {
//        Timber.e("----bindView---------$position----------------------------")
        if (obj is Chapter) {
            obj.run {
                itemView.run {
                    tv_name.text = name
                    if (!chapterHistory)
                        initChapterHistory(obj)
                    initTextColor(tv_name, obj.chapterHistory)
                    if (!reading)
                        initReading(obj)
                    icon_eye.show(reading)
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }

    fun initTextColor(tv: TextView, history: Boolean) {
        val textColor = if (history) R.color.text_purple else R.color.text_blue
        tv.setTextColor(ContextCompat.getColorStateList(mContext, textColor))
    }

    fun initChapterHistory(chapter: Chapter) {
        mCallbackChapterHistory?.initChapterHistory(chapter)
    }

    fun initReading(chapter: Chapter) {
        mCallbackChapterHistory?.initReading(chapter)
    }
}