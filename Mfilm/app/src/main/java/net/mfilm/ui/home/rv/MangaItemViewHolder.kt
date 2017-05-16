package net.mfilm.ui.home.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.rv.holders.BaseViewHolder
import net.mfilm.utils.*

/**
 * Created by tusi on 5/16/17.
 */
class MangaItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseViewHolder(mContext, type, itemView, mCallbackOnclick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is Manga) {
            obj.apply {
                itemView.apply {
                    tv_name.text = name
                    otherName.let { n ->
                        n?.apply {
                            tv_other_name.visibility = visible
                            tv_other_name.text = getTitleHeader(mContext, R.string.title_other_name, content = n)
                        } ?: let { tv_other_name.visibility = gone }
                    }
                    author.let { a ->
                        a?.apply {
                            tv_author.visibility = visible
                            tv_author.text = getTitleHeader(mContext, R.string.title_author, content = a)
                        } ?: let { tv_author.visibility = gone }
                    }
                    categories.let { ct ->
                        ct?.apply {
                            tv_categories.visibility = visible
                            tv_categories.text = getTitleHeader(mContext, R.string.title_categories,
                                    content = ct.map { it.name }.joinToString())
                        } ?: let { tv_categories.visibility = gone }
                    }
                    totalChap.let { c ->
                        c?.apply {
                            tv_chaps_count.visibility = visible
                            tv_chaps_count.text = getTitleHeader(mContext, R.string.title_chaps_count, content = c.toString())
                        } ?: let { tv_chaps_count.visibility = gone }
                    }
                    updatedTime.let { t ->
                        t?.apply {
                            tv_updated_at.visibility = visible
                            tv_updated_at.text = getTitleHeader(mContext, R.string.title_updated_at,
                                    content = TimeUtils.toFbFormatTime(mContext, t))
                        } ?: let { tv_updated_at.visibility = gone }
                    }
                    setOnClickListener { mCallbackOnClick?.onClick(position, type) }
                }
            }
        }
    }
}