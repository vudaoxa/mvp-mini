package net.mfilm.ui.mangas

import net.mfilm.data.db.models.SearchQueryRealm
import net.mfilm.data.network_retrofit.Manga
import net.mfilm.data.network_retrofit.MangasResponse
import net.mfilm.ui.base.MvpView
import net.mfilm.utils.*

/**
 * Created by Dieu on 09/03/2017.
 */

interface MangasMvpView : MvpView, ICallbackOnClick, ICallbackOnLongClick,
        ICallbackRefresh, ICallbackSpanCount, ICallbackSort, ICallbackDialogPlus,
        ICallbackDialogItemClicked, ICallbackFavInput {
    fun initRv()
    fun requestSearchHistory()
    fun onSearchHistoryResponse(searchHistoryRealms: List<SearchQueryRealm>?)
    fun onSearchHistoryNull()
    fun buildSearchHistory(searchHistoryRealms: List<SearchQueryRealm>)
    fun requestMangas()
    fun onMangasResponse(mangasResponse: MangasResponse?)
    fun onMangasNull()
    fun buildMangas(mangas: List<Manga>)
}
