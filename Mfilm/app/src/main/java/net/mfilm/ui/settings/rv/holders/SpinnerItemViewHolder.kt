package net.mfilm.ui.settings.rv.holders

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_spinner.view.*
import net.mfilm.R
import net.mfilm.data.prefs.MangaSources
import net.mfilm.ui.base.rv.holders.BaseItemViewHolder
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.ICallbackMangaSourcesHolder
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackSpnTracker

/**
 * Created by MRVU on 7/7/2017.
 */
class SpinnerItemViewHolder(mContext: Context, type: Int, itemView: View, mCallbackOnclick: ICallbackOnClick?)
    : BaseItemViewHolder(mContext, type, itemView, mCallbackOnclick), ICallbackSpnTracker, ICallbackMangaSourcesHolder {
    private var sources: MangaSources? = null
    override var mMangaSources: MangaSources?
        get() = sources
        set(value) {
            sources = value
        }
    override val spnFilterTracker = AdapterTracker({
        sort()
    })

    override fun bindView(obj: Any?, position: Int) {
        if (obj is MangaSources) {
            mMangaSources = obj
            itemView.run {
                tv_name.setText(obj.titleResId)
                val titles = obj.sources.map { it.title }
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
        mMangaSources?.sources?.run {
            mCallbackOnClick?.onClick(this[spnFilterTracker.mPosition].index, type)
        }
    }
}