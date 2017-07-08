package net.mfilm.ui.history

import net.mfilm.data.db.models.MangaHistoryRealm
import net.mfilm.ui.base.realm.RealmMvpView

/**
 * Created by tusi on 6/25/17.
 */
interface HistoryMvpView : RealmMvpView<MangaHistoryRealm> {
    fun onHistoryEnabled(enabled: Boolean)
    fun requestHistory()
    fun onHistoryResponse(mangaHistoryRealms: List<MangaHistoryRealm>?)
    fun onHistoryNull()
    fun buildHistory(mangaHistoryRealms: List<MangaHistoryRealm>)
    fun buildHistoryFilter(mangaHistoryRealms: List<MangaHistoryRealm>)
}