package net.mfilm.ui.mangas.search

import net.mfilm.ui.base.MvpPresenter
import net.mfilm.ui.mangas.MangasMvpView

/**
 * Created by tusi on 6/25/17.
 */
interface SearchHistoryMvpPresenter<V : MangasMvpView> : MvpPresenter<V> {
    fun saveQuery(query: String)
    fun requestSearchHistory()
}