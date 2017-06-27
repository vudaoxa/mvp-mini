package net.mfilm.ui.history

import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.MvpView
import net.mfilm.ui.manga.AdapterTracker
import net.mfilm.utils.ICallbackEmptyDataViewHolder
import net.mfilm.utils.ICallbackOnClick
import net.mfilm.utils.ICallbackReceiveOptionsMenu
import net.mfilm.utils.ICallbackSpanCount

/**
 * Created by tusi on 6/25/17.
 */
interface HistoryMvpView : MvpView, ICallbackOnClick,
        ICallbackSpanCount, ICallbackEmptyDataViewHolder, ICallbackReceiveOptionsMenu {
    val spnFilterTracker: AdapterTracker
    fun initRv()
    fun initSpnFilters()
    fun requestHistory()
    fun onHistoryResponse(mangaHistoryRealms: List<MangaHistoryRealm>?)
    fun onHistoryNull()
    fun buildHistory(mangaHistoryRealms: List<MangaHistoryRealm>)
}