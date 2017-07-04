package net.mfilm.ui.history

import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.ui.base.realm.RealmMvpPreseneter
import net.mfilm.utils.ICallbackBus

/**
 * Created by tusi on 6/25/17.
 */
interface HistoryMvpPresenter<V : HistoryMvpView> : MvpPresenter<V>, RealmMvpPreseneter, ICallbackBus {
    fun requestHistory()
    fun toggleHistory(mangaHistoryRealms: List<MangaHistoryRealm>)
}