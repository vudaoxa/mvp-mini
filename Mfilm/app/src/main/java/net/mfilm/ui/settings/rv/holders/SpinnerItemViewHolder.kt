package net.mfilm.ui.settings.rv.holders

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_spinner.view.*
import net.mfilm.R
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by MRVU on 7/7/2017.
 */
class SpinnerItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick) {
    val spnFilterTracker = AdapterTracker({
        sort()
    })

    override fun bindView(obj: Any?, position: Int) {
        if (obj is MangaSources) {
            itemView.run {
                var titles = obj.mangaSources.map { mContext.getString(it.titleResId) }
                if (obj.selectedIndex != 0) titles = titles.reversed() xx
                val banksAdapter = ArrayAdapter(mContext,
                        R.layout.item_spn_filter, titles)
                spn_source.run {
                    setAdapter(banksAdapter)
                    setOnItemSelectedListener(spnFilterTracker)
                }
            }
        }
    }

    fun sort() {
        mCallbackOnClick?.onClick(spnFilterTracker.mPosition, type)
    }
}