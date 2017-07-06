package net.mfilm.ui.search_history

import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.ui.base.MvpView

/**
 * Created by MRVU on 7/6/2017.
 */
interface SearchHistoryMvpView : MvpView {
    fun saveQuery(query: String)
    fun requestSearchHistory()
    fun onSearchHistoryResponse(searchHistoryRealms: List<SearchQueryRealm>?)
    fun onSearchHistoryNull()
    fun buildSearchHistory(searchHistoryRealms: List<SearchQueryRealm>)
    fun isHistoryVisible(): Boolean
    fun show(show: Boolean)
}