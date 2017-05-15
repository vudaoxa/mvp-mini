package net.mfilm.ui.home.rv

import android.content.Context
import android.support.v7.widget.RecyclerView
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.utils.ICallbackOnClick

/**
 * Created by tusi on 5/16/17.
 */
class MangaRvAdapter(val mContext: Context, var mangas: List<Manga>, val mCallbackOnClick: ICallbackOnClick)
    : RecyclerView.Adapter<> {
}