package net.mfilm.ui.mangas.search

import net.mfilm.ui.base.MvpPresenter
import net.mfilm.ui.search_history.SearchHistoryMvpView

/**
 * Created by tusi on 6/25/17.
 */
interface SearchHistoryMvpPresenter<V : SearchHistoryMvpView> : MvpPresenter<V> {
    fun saveQuery(query: String)
    fun requestSearchHistory()
}