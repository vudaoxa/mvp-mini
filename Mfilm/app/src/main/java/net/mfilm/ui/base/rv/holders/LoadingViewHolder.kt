package net.lc.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.layout_progress_view_small.view.*

/**
 * Created by mrvu on 12/28/16.
 */
class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var isIndeterminate = itemView.progressbar.isIndeterminate

    init {
    }
}
