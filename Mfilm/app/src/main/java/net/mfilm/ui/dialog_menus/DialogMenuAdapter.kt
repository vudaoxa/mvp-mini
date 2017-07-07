package net.mfilm.ui.dialog_menus

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_dialog_menu.view.*
import net.mfilm.ui.base.adapters.MBaseAdapter
import net.mfilm.ui.manga.DialogMenusItem

/**
 * Created by MRVU on 7/5/2017.
 */
class DialogMenuAdapter(private val mContext: Context, private val layoutResId: Int,
                        private val dialogItems: Array<DialogMenusItem>) : MBaseAdapter() {
    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dialogItems.size
    }

    override fun getItem(position: Int): DialogMenusItem? {
        return dialogItems.getOrNull(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var cv = convertView
//        Timber.e("--getView---------$position--------$cv------------------------------")
        var viewHolder: ViewHolder? = null
        cv?.run {
            viewHolder = cv?.tag as? ViewHolder?
        } ?: let {
            cv = LayoutInflater.from(mContext).inflate(layoutResId, parent, false)
            cv?.run {
                viewHolder = ViewHolder(this)
                convertView?.tag = viewHolder
            }
        }
        viewHolder?.view?.run {
            val x = getItem(position)
//            icon.setImageDrawable(x?.icon)
            tv_name.text = x?.title
        }

        return cv
    }
}