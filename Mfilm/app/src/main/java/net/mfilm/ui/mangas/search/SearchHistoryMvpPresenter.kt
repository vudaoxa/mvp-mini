package net.mfilm.ui.mangas.search

import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.MvpPresenter
import net.mfilm.ui.base.realm.RealmMvpPreseneter
import net.mfilm.ui.search_history.SearchHistoryMvpView

/**
 * Created by tusi on 6/25/17.
 */
interface SearchHistoryMvpPresenter<V : SearchHistoryMvpView> : MvpPresenter<V>, RealmMvpPreseneter {
    fun saveQuery(query: String)
    fun requestSearchHistory()
    fun toggleSearchHistory(searchQueryRealms: List<SearchQueryRealm>)
}