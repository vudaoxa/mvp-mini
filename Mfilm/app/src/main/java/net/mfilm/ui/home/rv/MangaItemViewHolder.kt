package net.mfilm.ui.home.rv

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_manga.view.*
import net.mfilm.R
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.ui.base.BaseViewHolder
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.getTitleHeader
import net.mfilm.utils.gone
import net.mfilm.utils.visible

/**
 * Created by tusi on 5/16/17.
 */
class MangaItemViewHolder(mContext: Context, itemView: View, val mCallbackOnclick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is Manga) {
            obj.apply {
                itemView.apply {
                    tv_name.text = name
                    author.let { a ->
                        a?.apply {
                            tv_author.visibility = visible
                            tv_author.text = getTitleHeader(mContext, R.string.title_author, content = a)
                        } ?: let { tv_author.visibility = gone }
                    }
                    otherName.let { n ->
                        n?.apply {
                            tv_other_name.visibility = visible
                            tv_other_name.text = getTitleHeader(mContext, R.string.title_author, content = n)
                        } ?: let { tv_other_name.visibility = gone }
                    }
                    xx
                }
            }
        }
    }
}